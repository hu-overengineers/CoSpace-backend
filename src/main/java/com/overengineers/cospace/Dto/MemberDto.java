package com.overengineers.cospace.Dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Builder
public class MemberDto {

    @Size(min = 1, max = 32, message = "Username can't be more than 32 characters!")
    public String username;

    @Size(min = 1, max = 32, message = "Password can't be more than 32 characters!")
    public final String password;

    @Size(min = 1, max = 44, message = "E-mail can't be more than 44 characters!")
    @Email(message = "Write a valid e-mail address")
    public String email;

    @JsonCreator
    public MemberDto(@JsonProperty("username") String username,
                     @JsonProperty("password") String password,
                     @JsonProperty("email") String email) {

        this.username = username;
        this.password = password;
        this.email = email;
    }
}
