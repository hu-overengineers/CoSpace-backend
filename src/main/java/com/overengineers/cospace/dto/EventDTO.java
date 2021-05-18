package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EventDTO {

    public final Long id;

    public final String title;

    public final String details;

    public final boolean isOnline;

    public final String location;

    public final String utilLink;

    public final String parentName;

}
