package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.dto.ReviewDTO;
import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.service.SubClubService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/subclub")
public class SubClubController {
    private final SubClubService subClubService;

    @GetMapping(value = "/all")
    public List<SubClubDTO> listAllSubClubs() {
        return subClubService.listAllSubClubs();
    }

    @GetMapping("/search")
    public List<SubClubDTO> search(@RequestParam(name = "query") String query, Pageable pageable){
        return subClubService.search(query,  pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/enroll")
    public ResponseEntity<String> enroll(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.enroll(subClubName);
    }

    @PostMapping("/review")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ReviewDTO review(@RequestBody ReviewDTO reviewDTO){
        return subClubService.reviewToSubClub(reviewDTO); // TODO: Auth check by enrollment
    }

    @GetMapping("/reviews")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<ReviewDTO> getReviewsByParentName(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.getReviewsByParentName(subClubName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/members")
    public List<MemberDTO> getSubClubMembers(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.getMembers(subClubName);
    }

    @GetMapping("/ban-check")
    public boolean isMemberBannedFromSubClub(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.isBanned(subClubName);
    }

}
