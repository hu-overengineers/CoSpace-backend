package com.overengineers.cospace.Controller;

import com.overengineers.cospace.Dto.ClubDTO;
import com.overengineers.cospace.Dto.SubClubDTO;
import com.overengineers.cospace.Entity.Club;
import com.overengineers.cospace.Entity.SubClub;
import com.overengineers.cospace.Mapper.ClubMapper;
import com.overengineers.cospace.Repository.ClubRepository;
import com.overengineers.cospace.Service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
