package com.overengineers.cospace.controller;


import com.overengineers.cospace.dto.LoginRequestDTO;
import com.overengineers.cospace.dto.LoginResponseDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.dto.RegisterRequestDTO;
import com.overengineers.cospace.entity.GenericResponse;
import com.overengineers.cospace.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


//@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PreAuthorize("permitAll")
    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest){
        return authService.login(loginRequest);
    }

    @PreAuthorize("permitAll")
    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return authService.register(registerRequestDTO);
    }

    @PreAuthorize("permitAll")
    @PostMapping(value = "/reset-password")
    public GenericResponse resetPassword(final HttpServletRequest request,
                                         @RequestParam("email") final String userEmail){
        return authService.resetPassword(request, userEmail);
    }

    @PreAuthorize("permitAll")
    @PostMapping("/change-password-token")
    public GenericResponse changePasswordWithToken(@RequestParam(name = "token") @NotBlank String token,
                                                   @RequestParam(name = "newPassword") @NotBlank String newPassword){
        return authService.changePasswordWithToken(token, newPassword);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/change-password")
    public GenericResponse changePassword(@RequestParam(name = "oldPassword") @NotBlank String oldPassword,
                                          @RequestParam(name = "newPassword") @NotBlank String newPassword) {
        return authService.changePassword(oldPassword, newPassword);
    }
    
}
