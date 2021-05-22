package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubClubCreateRequestDTO {

    private String subClubName;

    private String clubName;

    private String authorUsername;

}
