package com.overengineers.cospace.service;

import com.overengineers.cospace.auth.TokenManager;
import com.overengineers.cospace.dto.LoginRequestDTO;
import com.overengineers.cospace.dto.LoginResponseDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.dto.RegisterRequestDTO;
import com.overengineers.cospace.entity.GenericResponse;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.PasswordResetToken;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import java.util.*;

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
    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequest){
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        try{
            Authentication user = authenticationProvider.authenticate(authentication);

            String token = TokenManager.generateToken(user);

            /*
            // Ban check
            String username = TokenManager.getUsernameFromToken(token);
            Member member = memberRepository.findByUsername(username);
            Date now = Calendar.getInstance().getTime();

            if(member.getBan() != null && member.getBan().getEndDate().after(now)){
                throw new Exception();
            }
            */

            Member member = memberRepository.findByUsername(loginRequest.getUsername());

            // TODO: Add time-zone instead of Local Server Date
            member.setLastLogin(new Date());
            memberRepository.save(member);

            LoginResponseDTO loginResponseDTO = new LoginResponseDTO(token, TokenManager.getAuthorities(user));
            return new ResponseEntity<LoginResponseDTO>(loginResponseDTO, HttpStatus.OK);
        }
        catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401
                    .body(null);
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
    public ResponseEntity<String> register(RegisterRequestDTO registerRequestDTO){
        Member member = new Member(registerRequestDTO.getUsername(), registerRequestDTO.getPassword(),
                registerRequestDTO.getEmail(),null, null, null,
                null, null, null, null, null );

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
