package com.overengineers.cospace.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class RegisterRequestDTO {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty @Email
    private String email;

}
