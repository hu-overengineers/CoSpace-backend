package com.overengineers.cospace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class SearchResultsDTO {

    List<ClubDTO> clubs;

    List<SubClubDTO> subClubs;

    List<MemberDTO> members;

    List<PostDTO> posts;

}

