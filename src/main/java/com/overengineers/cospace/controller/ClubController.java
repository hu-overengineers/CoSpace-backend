package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.service.ClubService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubController {

    private final ClubService clubService;

    @PreAuthorize("permitAll")
    @GetMapping(value = "/all")
    public List<ClubDTO> listAllClubs() {
        return clubService.listAllClubs();
    }

    @PreAuthorize("permitAll")
    @GetMapping("/search")
    public List<ClubDTO> search(@RequestParam(name = "query") String query, Pageable pageable){
        return clubService.search(query, pageable);
    }

}
