package com.overengineers.cospace.Controller;


import com.overengineers.cospace.Auth.TokenManager;
import com.overengineers.cospace.Dto.LoginRequest;
import com.overengineers.cospace.Dto.MemberDTO;
import com.overengineers.cospace.Entity.Member;
import com.overengineers.cospace.Mapper.MemberMapper;
import com.overengineers.cospace.Service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

//@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            return ResponseEntity.ok(tokenManager.generateToken(loginRequest.getUsername()));
        } catch(Exception e) {
            throw(e);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String > register(@Valid @RequestBody MemberDTO memberDTO) {
        Member member = memberMapper.mapToEntity(memberDTO);
        if (memberService.register(member) != null)
            return ResponseEntity.ok("Registered successfully.");
        else
            return ResponseEntity.ok("Registration Error!");
    }
}
