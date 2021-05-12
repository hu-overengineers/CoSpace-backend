package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.ClubMapper;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.repository.ClubRepository;
import com.overengineers.cospace.service.AdminService;
import com.overengineers.cospace.service.ClubService;
import com.overengineers.cospace.service.SubClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping(value = "/create-club")
    public ClubDTO createClub(@Valid @RequestBody ClubDTO clubDTO) {
        return adminService.createClub(clubDTO);
    }

    @PostMapping(value = "/create-sub-club")
    public SubClubDTO createSubClub(@RequestBody SubClubDTO subClubDTO){
        return adminService.createSubClub(subClubDTO);
    }
}