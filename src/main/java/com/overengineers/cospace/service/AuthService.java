package com.overengineers.cospace.service;

import com.overengineers.cospace.auth.TokenManager;
import com.overengineers.cospace.dto.LoginRequest;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.entity.GenericResponse;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.PasswordResetToken;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final DaoAuthenticationProvider authenticationProvider;
    private final CustomUserDetailsManager customUserDetailsManager;

    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    private final MessageSource messages;

    private final MailService mailService;
    private final PasswordResetTokenRepository passwordTokenRepository;

    @Transactional
    public ResponseEntity<?> login(LoginRequest loginRequest){
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        try{
            Authentication user = authenticationProvider.authenticate(authentication);
            String token = TokenManager.generateToken(user);
            JSONObject resp = new JSONObject();
            resp.put("token", token);
            resp.put("auth", TokenManager.getAuthorities(user));

            return new ResponseEntity<String>(resp.toString(), HttpStatus.OK);
        }
        catch (AuthenticationException | JSONException e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401
                    .body("Login Error!");
        }
    }

    public String getAuthenticatedUsername(){
        try{
            String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            return username;
        }
        catch (Exception e){
            System.out.println("getAuthenticatedUsername user not found!");
            return null;
        }
    }

    @Transactional
    public ResponseEntity<String> register(MemberDTO memberDTO){
        Member member = memberMapper.mapToEntity(memberDTO);
        if(customUserDetailsManager.userExistsByEmail(member.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409
                    .body("Member with that email already exists!");
        }

        if(customUserDetailsManager.userExists(member.getUsername())){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409
                    .body("Member with that username already exists!");
        }

        // Success Case
        customUserDetailsManager.createUser(member);
        return ResponseEntity.ok("You are successfully registered.");
    }


    public String createPasswordResetTokenForMember(Member member) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken(token, member);
        passwordTokenRepository.save(myToken);
        return token;
    }

    @Transactional
    public GenericResponse resetPassword(final HttpServletRequest request, String userEmail) {
        Member member = memberRepository.findByEmail(userEmail);

        if (member == null) {
            return null;
        }

        String token = createPasswordResetTokenForMember(member);
        return mailService.sendResetPasswordMail(request, member, token);
    }

    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    public Optional<Member> getMemberByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getMember());
    }

    @Transactional
    public GenericResponse changePasswordWithToken(String token, String newPassword){
        String tokenValidationError = validatePasswordResetToken(token);

        if(tokenValidationError != null) {
            return new GenericResponse(messages.getMessage(
                    "auth.message." + tokenValidationError, null, Locale.ENGLISH));
        }

        Optional<Member> member = getMemberByPasswordResetToken(token);
        if(member.isPresent()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            member.get().setPassword(passwordEncoder.encode(newPassword));
            memberRepository.save(member.get());
            passwordTokenRepository.deleteByToken(token);
            return new GenericResponse(messages.getMessage(
                    "message.resetPasswordSuc", null, Locale.ENGLISH));
        } else {
            return new GenericResponse(messages.getMessage(
                    "auth.message.invalid", null, Locale.ENGLISH));
        }
    }

    @Transactional
    public GenericResponse changePassword(String oldPassword, String newPassword){
        if(getAuthenticatedUsername() == null){
            return new GenericResponse(messages.getMessage(
                    "message.unauth", null, Locale.ENGLISH));
        }

        try{
            customUserDetailsManager.changePassword(oldPassword, newPassword);
            return new GenericResponse(messages.getMessage(
                    "message.updatePasswordSuc", null, Locale.ENGLISH));
        }
        catch (Exception e){
            // Wrong old password
            System.out.println(e.getMessage());
            return new GenericResponse(messages.getMessage(
                    "message.invalidOldPassword", null, Locale.ENGLISH));
        }
    }

}
