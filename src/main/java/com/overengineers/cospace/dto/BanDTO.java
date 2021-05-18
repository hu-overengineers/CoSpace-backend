package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class BanDTO {
    private String reason;

    private Date endDate;

    private int count;

    private boolean admBan;

    private String subClubName;

    private String memberName;
}
