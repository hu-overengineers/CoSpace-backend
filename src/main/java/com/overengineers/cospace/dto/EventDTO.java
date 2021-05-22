package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class EventDTO {

    public final Long id;

    public final String title;

    public final String details;

    public final Date date;

    public final Boolean isOnline;

    public final String location;

    public final String utilLink;

    public final String parentName;

    public final Integer numberOfParticipants;

}
