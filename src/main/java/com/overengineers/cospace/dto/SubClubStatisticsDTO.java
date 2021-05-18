package com.overengineers.cospace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SubClubStatisticsDTO {
    // Response
    public int numberOfMembers;
    public int numberOfPostsInTimeFrame;

}
