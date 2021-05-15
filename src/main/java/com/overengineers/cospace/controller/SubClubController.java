package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.service.SubClubService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/subclub")
public class SubClubController {
    private final SubClubService subClubService;

    @GetMapping(value = "/all")
    public List<SubClubDTO> listAllSubClubs() {
        return subClubService.listAllSubClubs();
    }

    @PostMapping("/rate")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public SubClubDTO rate(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.rate(subClubName); // TODO: Auth check by enrollment
    }
}
