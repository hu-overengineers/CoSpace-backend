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

    @PostMapping(value = "/create-club")
    public ClubDTO createClub(@Valid @RequestBody ClubDTO clubDTO) {
        return adminService.createClub(clubDTO);
    }

    @PostMapping(value = "/create-sub-club")
    public SubClubDTO createSubClub(@RequestBody SubClubDTO subClubDTO){
        return adminService.createSubClub(subClubDTO);
    }

    @GetMapping(value = "/reports")
    public List<ReportDTO> getReports(){
        return adminService.getAllReports();
    }

    @GetMapping(value = "/all-members")
    public List<MemberDTO> getAllMembers(){
        List<Member> memberList = memberRepository.findAll();
        return memberMapper.mapToDto(memberList);
    }
}