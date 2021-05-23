package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Getter
@Builder
public class MemberDTO {

    public String username;

    @Email(message = "Write a valid e-mail address")
    public String email;

    public Date created;

    public Date lastLogin;

    public List<EventDTO> attendedEvents;

}
