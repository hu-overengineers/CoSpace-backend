package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.dto.PrivateMessageDTO;
import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.PrivateMessage;
import com.overengineers.cospace.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/enrolled-subclubs")
    public List<SubClubDTO> getEnrolledSubClubs(){
        return memberService.getEnrolledSubClubs();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/search")
    public List<MemberDTO> searchMember(@RequestParam("query") String query, @RequestParam("parentName") String parentName, Pageable pageable) throws Exception {
        // query should include substring of the username
        return memberService.search(query, parentName, pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = "/private-message")
    public List<PrivateMessageDTO> getPrivateMessages(){
        return memberService.getPrivateMessages();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/private-message")
    public PrivateMessageDTO sendPrivateMessage(@RequestBody PrivateMessageDTO privateMessageDTO){
        return memberService.sendPrivateMessage(privateMessageDTO);
    }



}
