package com.overengineers.cospace.Dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
@Builder
public class ClubDTO {
    @Size(min = 1, max = 32, message = "Club name can't be more than 32 characters!")
    public String clubName;

    @Size(min = 1, max = 255, message = "Club details can't be more than 255 characters!")
    public final String details;

    @JsonCreator
    public ClubDTO(@JsonProperty("clubName") String clubName,
                   @JsonProperty("details") String details) {
        this.clubName = clubName;
        this.details = details;
    }
}
