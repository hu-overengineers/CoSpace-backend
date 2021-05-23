package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ClubDTO {

    public String name;

    public final String details;

    public Date created;

}
