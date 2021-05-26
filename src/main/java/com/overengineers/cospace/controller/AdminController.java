package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.*;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.entity.Question;
import com.overengineers.cospace.mapper.BanMapper;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.mapper.QuestionMapper;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final QuestionMapper questionMapper;
    private final BanMapper banMapper;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/create-club")
    public ClubDTO createClub(@Valid @RequestBody ClubDTO clubDTO) {
        return adminService.createClub(clubDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/create-sub-club")
    public SubClubDTO createSubClub(@RequestBody SubClubDTO subClubDTO){
        SubClubDTO savedSubClubDTO = adminService.createSubClub(subClubDTO);

        if(savedSubClubDTO == null){
            System.out.println("Club: " + subClubDTO.getParentName() + " is not found!");
            return null;
        }

        if(savedSubClubDTO.getQuestions() != null)
            savedSubClubDTO.getQuestions().clear();

        for(QuestionDTO questionDTO : subClubDTO.getQuestions()){
            Question currentQuestion = adminService.createQuestion(questionDTO, subClubDTO.getName());
            QuestionDTO currentQuestionDTO = questionMapper.mapToDto(currentQuestion);
            savedSubClubDTO.getQuestions().add(currentQuestionDTO);
        }

        return savedSubClubDTO;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/delete-sub-club")
    public boolean deleteSubClub(@RequestParam(name = "id") long id){
        return adminService.deleteSubClub(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping(value = "/subclub")
    public SubClubDTO updateSubClub(@RequestBody SubClubDTO subClubDTO){

        SubClubDTO savedSubClubDTO = adminService.updateSubClub(subClubDTO);
        return adminService.updateSubClubUtil(subClubDTO, savedSubClubDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/reports")
    public List<ReportDTO> getReports(){
        return adminService.getAllReports();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(value = "/delete-report")
    public boolean deleteReport(@RequestParam(name = "reportId") long reportId){
        return adminService.deleteReport(reportId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping(value = "/kick-member")
    public ResponseEntity<MemberDTO> kickMemberFromCoSpace(@RequestParam(name = "username") String username){
        // TODO: delete on table "member" violates foreign key constraint on table "enrollment"
        Member member = memberRepository.findByUsername(username);
        if (member != null) {
            adminService.kickMemberFromCoSpace(username);
            return ResponseEntity.ok(memberMapper.mapToDto(member));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/all-members")
    public List<MemberDTO> getAllMembers(){
        List<Member> memberList = memberRepository.findAll();
        return memberMapper.mapToDto(memberList);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/enrolled-subclubs")
    public List<SubClubDTO> getEnrolledSubClubs(@RequestParam("username") String username) throws Exception {
        return adminService.getEnrolledSubClubs(username);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/search-member")
    public List<MemberDTO> searchMember(@RequestParam("query") String query, Pageable pageable) throws Exception {
        // query should include substring of the username
        return adminService.searchMember(query, pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/subclub-requests")
    public List<SubClubCreateResponseDTO> getSubClubCreateRequests(){
        return adminService.getSubClubCreateRequests();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/mod-requests")
    public List<String> getModeratorNeededSubClubs(){
        return adminService.getModeratorNeedSubClubs();
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
    @PostMapping(value = "/ban-moderator")
    public BanDTO banModerator(@RequestParam(name = "username") String username,
                                  @RequestParam(name = "reason") String reason){
        return banMapper.mapToDto(adminService.banModerator(username, reason));
    }

}
