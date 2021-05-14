package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping(value = "/enrolled-clubs")
    public List<ClubDTO> getEnrolledClubs(){
        return memberService.getEnrolledClubs();
    }
}
