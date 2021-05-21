package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnrollmentDTO {

    public final String memberUsername;

    public final String subClubName;

    public final float interestRate;

}
