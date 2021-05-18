package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class EventDTO {

    public final Long id;

    public final String title;

    public final String details;

    public final String date;

    public final String isOnline;

    public final String location;

    public final String utilLink;

    public final String parentName;

}
