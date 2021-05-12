package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.repository.ClubRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import com.overengineers.cospace.service.SubClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/subclub")
public class SubClubController {
    private final SubClubService subClubService;
    private final SubClubMapper subClubMapper;

    @GetMapping(value = "/all")
    public List<SubClubDTO> listAllSubClubs() {
        return subClubService.listAllSubClubs();
    }

}
