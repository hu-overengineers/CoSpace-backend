package com.overengineers.cospace.controller;


import com.overengineers.cospace.dto.LoginRequest;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.entity.GenericResponse;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

//@Validated
@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@Valid @RequestBody MemberDTO memberDTO) {
        return authService.register(memberDTO);
    }

    @PostMapping(value = "/reset-password")
    public GenericResponse resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail){
        return authService.resetPassword(request, userEmail);
    }


}
