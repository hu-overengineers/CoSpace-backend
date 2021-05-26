package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.*;
import com.overengineers.cospace.mapper.ClubMapper;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.mapper.PostMapper;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    private final ClubMapper clubMapper;
    private final SubClubMapper subClubMapper;
    private final MemberMapper memberMapper;
    private final PostMapper postMapper;


    @PreAuthorize("permitAll")
    @GetMapping
    public ResponseEntity<SearchResultsDTO> performSystemWideSearch(@RequestParam(name = "query") String query, Pageable pageable) {

        List<ClubDTO> clubs = clubMapper.mapToDto(searchService.searchClubs(query, pageable).toList());
        List<SubClubDTO> subClubs = subClubMapper.mapToDto(searchService.searchSubClubs(query, pageable).toList());
        List<MemberDTO> members = memberMapper.mapToDto(searchService.searchMembers(query, pageable).toList());
        List<PostDTO> posts = postMapper.mapToDto(searchService.searchPosts(query, pageable).toList());

        SearchResultsDTO searchResults = new SearchResultsDTO(
                clubs,
                subClubs,
                members,
                posts
        );

        return ResponseEntity.ok(searchResults);
    }


}
