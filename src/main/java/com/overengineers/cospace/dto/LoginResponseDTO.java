package com.overengineers.cospace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {

    public String token;
    public List<String> auth;

}
