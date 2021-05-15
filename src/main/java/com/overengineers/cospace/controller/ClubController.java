package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.service.ClubService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubController {
    private final ClubService clubService;

    private final MemberRepository memberRepository;

    @GetMapping(value = "/all")
    public List<ClubDTO> listAllClubs() {
        return clubService.listAllClubs();
    }

    @PostMapping("/rate")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ClubDTO rate(@RequestParam(name = "clubName") String clubName){
        return clubService.rate(clubName); // TODO: Auth check by enrollment
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/enroll")
    public ResponseEntity<String> enrollClub(@RequestParam(name = "clubName") String clubName){
        return clubService.enrollClub(clubName);
    }

}
