package com.overengineers.cospace.service;

import com.overengineers.cospace.auth.TokenManager;
import com.overengineers.cospace.dto.LoginRequest;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final DaoAuthenticationProvider authenticationProvider;

    private final CustomUserDetailsManager customUserDetailsManager;
    private final MemberMapper memberMapper;

    public ResponseEntity<String> login(LoginRequest loginRequest){
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());


        try{
             Authentication user = authenticationProvider.authenticate(authentication);
            String token = TokenManager.generateToken(user);
            return ResponseEntity.ok(token);
        }
        catch (AuthenticationException e){
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity<String> register(MemberDTO memberDTO){
        Member member = memberMapper.mapToEntity(memberDTO);
        if(customUserDetailsManager.userExistsByEmail(member.getEmail())) {
            return ResponseEntity.ok("Member with that email already exists!");
        }

        if(customUserDetailsManager.userExists(member.getUsername())){
            return ResponseEntity.ok("Member with that username already exists!");
        }

        // Success Case
        customUserDetailsManager.createUser(member);
        return ResponseEntity.ok("You are successfully registered.");
    }



}
