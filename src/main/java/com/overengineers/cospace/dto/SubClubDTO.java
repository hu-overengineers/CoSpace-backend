package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Builder
public class SubClubDTO {

    @Size(min = 1, max = 32, message = "Club name can't be more than 32 characters!")
    public final String name;

    @Size(min = 1, max = 255, message = "Club details can't be more than 255 characters!")
    public final String details;

    public final String parentName;

    public final int rating;

    public final String moderatorUsername;
    
    public final int id;

    public LocalDateTime created;

}
