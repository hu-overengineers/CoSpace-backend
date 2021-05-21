package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.EventDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.dto.ReviewDTO;
import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.dto.SubClubStatisticsDTO;
import com.overengineers.cospace.service.SecurityService;
import com.overengineers.cospace.service.SubClubService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/subclub")
public class SubClubController {
    private final SubClubService subClubService;
    private final SecurityService securityService;

    @PreAuthorize("permitAll")
    @GetMapping(value = "/all")
    public List<SubClubDTO> listAllSubClubs() {
        return subClubService.listAllSubClubs();
    }


    @PreAuthorize("permitAll")
    @GetMapping(value = "/all-with-clubname")
    public List<SubClubDTO> listByParentName(@RequestParam(name = "clubName") String clubName){
        return subClubService.listByParentName(clubName);
    }

    @PreAuthorize("permitAll")
    @GetMapping("/search")
    public List<SubClubDTO> search(@RequestParam(name = "query") String query, Pageable pageable){
        return subClubService.search(query,  pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/enroll")
    public ResponseEntity<String> enroll(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.enroll(subClubName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/review")
    public ReviewDTO review(@RequestBody ReviewDTO reviewDTO){
        return subClubService.reviewToSubClub(reviewDTO); // TODO: Auth check by enrollment
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/reviews")
    public List<ReviewDTO> getReviewsByParentName(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.getReviewsByParentName(subClubName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/members")
    public List<MemberDTO> getSubClubMembers(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.getMembers(subClubName);
    }

    @PreAuthorize("permitAll")
    @GetMapping("/events")
    public List<EventDTO> getSubClubEvents(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.getEvents(subClubName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/mod-request")
    public ResponseEntity<String> sendModRequest(@RequestParam("subClubName") String subClubName){
        return subClubService.sendModRequest(subClubName);
    }

    @PreAuthorize("permitAll")
    @GetMapping("/statistics")
    public SubClubStatisticsDTO getStatistics(@RequestParam(name = "subClubName") String subClubName,
                                              @RequestParam(name = "timeStart") Long timeStart,
                                              @RequestParam(name = "timeEnd") Long timeEnd) {
        return subClubService.getStatistics(subClubName,
                Date.from(Instant.ofEpochMilli(timeStart)),
                Date.from(Instant.ofEpochMilli(timeEnd)));
    }

    @GetMapping("/ban-check")
    public boolean amIBanned(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.isBanned(subClubName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/common-subclubs")
    public List<SubClubDTO> getCommonSubClubs(@RequestParam(name = "username") String username) {
        String currentlySignedInMemberUsername = securityService.getAuthorizedUsername();
        return subClubService.getCommonSubClubs(currentlySignedInMemberUsername, username);
    }

}
