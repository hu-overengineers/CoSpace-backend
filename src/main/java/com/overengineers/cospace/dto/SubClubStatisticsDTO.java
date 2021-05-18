package com.overengineers.cospace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SubClubStatisticsDTO {

    public int numberOfMembers;
    public int numberOfPostsInTimeFrame;

}
