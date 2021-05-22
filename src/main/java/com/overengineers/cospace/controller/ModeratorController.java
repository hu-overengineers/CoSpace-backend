package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.*;
import com.overengineers.cospace.mapper.BanMapper;
import com.overengineers.cospace.mapper.EnrollmentMapper;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.mapper.ReportMapper;
import com.overengineers.cospace.service.ModeratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moderator")
public class ModeratorController {

    private final ModeratorService moderatorService;

    private final EnrollmentMapper enrollmentMapper;
    private final MemberMapper memberMapper;
    private final BanMapper banMapper;
    private final ReportMapper reportMapper;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/ban")
    public BanDTO banMemberFromSubClub(@RequestParam(name = "username") String username,
                                       @RequestParam(name = "subClubName") String subClubName,
                                       @RequestParam(name = "reason") String reason){
        return banMapper.mapToDto(moderatorService.banMemberFromSubClub(username, subClubName, reason));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/event")
    public EventDTO createEvent(@RequestBody EventDTO eventDTO){
        return moderatorService.createEvent(eventDTO);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping(value = "/event")
    public EventDTO updateEvent(@RequestBody EventDTO eventDTO){
        return moderatorService.updateEvent(eventDTO);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping(value = "/event")
    public boolean deleteEvent(@RequestBody EventDTO eventDTO){
        return moderatorService.deleteEvent(eventDTO);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/reports")
    public List<ReportDTO> getReports(){
        return reportMapper.mapToDto(moderatorService.getReports());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/delete-report")
    public boolean deleteReport(@RequestParam(name = "reportId") long reportId){
        return moderatorService.deleteReport(reportId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/delete-post")
    public boolean deletePost(@RequestParam(name = "postId") long postId){
        return moderatorService.deletePost(postId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/dismiss-list")
    public List<MemberDTO> getDismissibleList(){
        return memberMapper.mapToDto(moderatorService.getDismissibleList());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/dismiss")
    public EnrollmentDTO dismissMemberFromSubClub(@RequestParam(name = "username") String username){
        return enrollmentMapper.mapToDto(moderatorService.dismissMemberFromSubClub(username));
    }

}
