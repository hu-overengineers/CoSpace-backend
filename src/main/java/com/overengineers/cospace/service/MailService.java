package com.overengineers.cospace.service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;


import com.overengineers.cospace.entity.GenericResponse;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.SubClub;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    private final Environment env;

    private MimeMessage constructEmail(String subject, String body, String email) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setText(body, true); // Use this or above line.
            helper.setSubject(subject);
            helper.setTo(email);
            helper.setFrom(env.getProperty("support.email"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mimeMessage;
    }

    private MimeMessage constructResetTokenEmail(final String contextPath, final String token, final Member member) {
        final String url = contextPath + "/password-reset/" + token;
        final String mailSubTitle = messages.getMessage("message.resetPassword", null, Locale.ENGLISH);
        final String mailLinkText = messages.getMessage("message.resetPasswordEmailLink", null, Locale.ENGLISH);

        return constructEmail("CoSpace - Reset Password", "<b>" + mailSubTitle + "</b><br>" + "<a href=" + url + ">" + mailLinkText + "</a>", member.getEmail());
    }

    public GenericResponse sendResetPasswordMail(final HttpServletRequest request, Member member, String token) {
        final String baseUrl = "http://localhost:3000";
        // TODO: baseUrl will change with change-password-token fronend page link
        mailSender.send(constructResetTokenEmail(baseUrl, token, member));
        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, Locale.ENGLISH));
        // Locale.ENGLISH can be replaced with request.getLocale() for internationalization
    }

    private MimeMessage constructSubClubDeleteEmail(String baseUrl, String email, String username, String subClubName) {
        final String mailSubTitle = "SubClub:" + subClubName + " will be deleted!";

        return constructEmail("CoSpace - " + subClubName, "Dear " + username + ",<br>" + "<b>" + mailSubTitle + "</b>", email);
    }

    public GenericResponse sendSubClubDeleteMail(Map<String, String> mailList, String subClubName) {
        final String baseUrl = "http://localhost:3000";
        for (String username : mailList.keySet()) {
            mailSender.send(constructSubClubDeleteEmail(baseUrl, mailList.get(username), username, subClubName));
        }
        return new GenericResponse(messages.getMessage("message.deleteSubClubMessage", null, Locale.ENGLISH));
    }


}