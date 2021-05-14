package com.overengineers.cospace.service;

import com.overengineers.cospace.auth.TokenManager;
import com.overengineers.cospace.dto.LoginRequest;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
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
        catch (AuthenticationException e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // 401
                    .body("Login Error!");
        }
    }

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
}
