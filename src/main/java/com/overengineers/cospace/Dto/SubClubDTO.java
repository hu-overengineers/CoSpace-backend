package com.overengineers.cospace.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.overengineers.cospace.Entity.Club;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
@Builder
public class SubClubDTO {
    @Size(min = 1, max = 32, message = "Club name can't be more than 32 characters!")
    public final String subClubName;

    @Size(min = 1, max = 255, message = "Club details can't be more than 255 characters!")
    public final String details;

    public final String upperClubName;

    @JsonIgnore
    public Club upperClub;
}
