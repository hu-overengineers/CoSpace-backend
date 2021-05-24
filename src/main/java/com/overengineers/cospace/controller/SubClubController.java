package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.*;
import com.overengineers.cospace.mapper.EventMapper;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.mapper.QuestionMapper;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.service.SecurityService;
import com.overengineers.cospace.service.SubClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subclub")
public class SubClubController {
    private final SubClubService subClubService;
    private final SecurityService securityService;

    private final SubClubMapper subClubMapper;
    private final MemberMapper memberMapper;
    private final QuestionMapper questionMapper;
    private final EventMapper eventMapper;

    @PreAuthorize("permitAll")
    @GetMapping(value = "/all")
    public List<SubClubDTO> listAllSubClubs() {
        return subClubMapper.mapToDto(subClubService.listAllSubClubs());
    }

    @PreAuthorize("permitAll")
    @GetMapping(value = "/all-with-clubname")
    public List<SubClubDTO> listByParentName(@RequestParam(name = "clubName") String clubName){
        return subClubMapper.mapToDto(subClubService.listByParentName(clubName));
    }

    @PreAuthorize("permitAll")
    @GetMapping("/search")
    public List<SubClubDTO> search(@RequestParam(name = "query") String query, Pageable pageable){
        return subClubMapper.mapToDto(subClubService.search(query,  pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/questionnaire")
    public List<QuestionDTO> getQuestionnaire(){
        return questionMapper.mapToDto(subClubService.getQuestionnaire());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/questionnaire-sub")
    public List<QuestionDTO> getQuestionnaireBySubClub(@RequestParam(name = "subClubName") String subClubName){
        return questionMapper.mapToDto(subClubService.getQuestionnaireBySubClub(subClubName));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/enroll")
    public ResponseEntity<String> enroll(@RequestBody List<QuestionDTO> answers){
        return subClubService.enroll(answers);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/enroll-subclub")
    public ResponseEntity<String> enrollSubClub(@RequestBody List<QuestionDTO> answers){
        return subClubService.enrollSubClub(answers);
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
        return memberMapper.mapToDto(subClubService.getMembers(subClubName));
    }

    @PreAuthorize("permitAll")
    @GetMapping("/events")
    public List<EventDTO> getSubClubEvents(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.getEvents(subClubName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/attend-event")
    public EventDTO attendEvent(@RequestParam(name = "eventId") Long eventId) {
        return eventMapper.mapToDto(subClubService.attendEvent(eventId));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/mod-request")
    public ResponseEntity<String> sendModRequest(@RequestParam("subClubName") String subClubName){
        return subClubService.sendModRequest(subClubName);
    }

    @PreAuthorize("permitAll")
    @GetMapping("/statistics")
    public SubClubStatisticsDTO getStatistics(@RequestParam(name = "subClubName") String subClubName,
                                              @RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
                                              @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
        return subClubService.getStatistics(subClubName, start, end);
    }

    @GetMapping("/ban-check")
    public boolean amIBanned(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.isBanned(subClubName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/common-subclubs")
    public List<SubClubDTO> getCommonSubClubs(@RequestParam(name = "username") String username) {
        String currentlySignedInMemberUsername = securityService.getAuthorizedUsername();
        return subClubMapper.mapToDto(subClubService.getCommonSubClubs(currentlySignedInMemberUsername, username));
    }

}
