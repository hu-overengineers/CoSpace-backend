package com.overengineers.cospace.Dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
@Builder
public class ClubDTO {
    @Size(min = 1, max = 32, message = "Club name can't be more than 32 characters!")
    public String name;

    @Size(min = 1, max = 255, message = "Club description can't be more than 255 characters!")
    public final String description;
}
