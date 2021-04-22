package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.mapper.ClubMapper;
import com.overengineers.cospace.repository.ClubRepository;
import com.overengineers.cospace.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubController {
    private final ClubService clubService;
    private final ClubMapper clubMapper;
    private final ClubRepository clubRepository;
    // Everyone
    @GetMapping(value = "/all-clubs")
    public List<ClubDTO> listAllClubs() {
        List<Club> clubList = clubService.listAllClubs();
        return clubMapper.mapToDto(clubList);
    }

    // Admin
    @PostMapping(value = "/create")
    public ClubDTO createClub(@Valid @RequestBody ClubDTO clubDTO) {
        Club club = clubMapper.mapToEntity(clubDTO);
        Club newClub = clubService.saveNewClub(club);
        return clubMapper.mapToDto(newClub);
    }






}
