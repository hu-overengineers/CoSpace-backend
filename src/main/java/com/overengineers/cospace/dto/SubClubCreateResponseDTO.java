package com.overengineers.cospace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SubClubCreateResponseDTO {

    private String clubName;

    private List<String> subClubNameList;

    private int requestCount;

}
