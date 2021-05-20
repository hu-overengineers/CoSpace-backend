package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.dto.ReportDTO;
import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/create-club")
    public ClubDTO createClub(@Valid @RequestBody ClubDTO clubDTO) {
        return adminService.createClub(clubDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/create-sub-club")
    public SubClubDTO createSubClub(@RequestBody SubClubDTO subClubDTO){
        return adminService.createSubClub(subClubDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/reports")
    public List<ReportDTO> getReports(){
        return adminService.getAllReports();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/all-members")
    public List<MemberDTO> getAllMembers(){
        List<Member> memberList = memberRepository.findAll();
        return memberMapper.mapToDto(memberList);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/mod-requests") // TODO: This endpoint is used for development, remove for final product
    public List<MemberDTO> getModeratorRequests(@RequestParam(name = "subClubName") String subClubName){
        return adminService.getModeratorRequestsDTO(subClubName);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/make-moderator") // TODO: This endpoint is used for development, remove for final product
    public SubClubDTO makeModerator(@RequestParam(name = "username") String username,
                                   @RequestParam(name = "subClubName") String subClubName){
        return adminService.makeModerator(username, subClubName);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/assign-moderator")
    public SubClubDTO assignModeratorRandomly(@RequestParam(name = "subClubName") String subClubName){
        return adminService.assignModeratorRandomly(subClubName);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/ban-mod")
    public MemberDTO banMember(@RequestParam(name = "username") String username){
        return adminService.banMember(username); // NOT IMPLEMENTED
    }

    // getReports about the moderators

    // ban moderator of subclub with modban (maybe user check in security service ban function, and if auth=admin and username=subclub mod, make admban true)

    // get moderator requests
}