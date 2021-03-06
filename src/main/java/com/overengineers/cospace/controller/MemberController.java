package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.*;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.*;
import com.overengineers.cospace.service.EnrollmentService;
import com.overengineers.cospace.service.MemberService;
import com.overengineers.cospace.service.PrivateMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final PrivateMessagingService privateMessagingService;
    private final EnrollmentService enrollmentService;

    private final MemberMapper memberMapper;
    private final SubClubMapper subClubMapper;
    private final PrivateMessageMapper privateMessageMapper;
    private final EventMapper eventMapper;
    private final SubClubCreateRequestMapper subClubCreateRequestMapper;
    private final EnrollmentMapper enrollmentMapper;

    @PreAuthorize("permitAll")
    @GetMapping
    public MemberDTO getMemberByName(@RequestParam(name = "username") String username){
        return memberMapper.mapToDto(memberService.getMemberByName(username));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/enrolled-subclubs")
    public List<SubClubDTO> getEnrolledSubClubs() {
        List<SubClub> subClubs = memberService.getEnrolledSubClubs();
        return subClubMapper.mapToDto(subClubs);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/attended-events")
    public List<EventDTO> getAttendedEvents() {
        return eventMapper.mapToDto(memberService.getAttendedEvents());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/search")
    public List<MemberDTO> searchMember(@RequestParam("query") String query, @RequestParam("parentName") String parentName, Pageable pageable) throws Exception {
        // query should include substring of the username
        return memberMapper.mapToDto(memberService.searchWithinTheSameSubClub(query, parentName, pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/private-message")
    public List<PrivateMessageDTO> getPrivateMessages() {
        return privateMessageMapper.mapToDto(privateMessagingService.getAllMessagesOfAuthorizedMember());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/private-message")
    public PrivateMessageDTO sendPrivateMessage(@RequestBody PrivateMessageDTO privateMessageDTO) {
        return privateMessageMapper.mapToDto(privateMessagingService.send(privateMessageDTO));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/request-subclub")
    public SubClubCreateRequestDTO requestSubClub(@RequestBody SubClubCreateRequestDTO subClubCreateRequestDTO) {
        return subClubCreateRequestMapper.mapToDto(memberService.requestSubClub(subClubCreateRequestDTO));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/enrollment")
    public List<EnrollmentDTO> getMyEnrollmentList(@RequestParam(name = "username") String username){
        return enrollmentMapper.mapToDto(enrollmentService.getCommonEnrollmentList(username));
    }


}
