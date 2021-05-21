package com.overengineers.cospace.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginRequestDTO {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}
