package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.repository.ClubRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import com.overengineers.cospace.service.SubClubService;
import lombok.RequiredArgsConstructor;
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
    private final SubClubRepository subClubRepository;
    private final ClubRepository clubRepository;


    @GetMapping(value = "/all-subclubs")
    public List<SubClubDTO> listAllSubClubs() {
        List<SubClub> subClubList = subClubService.listAllSubClubs();
        return subClubMapper.mapToDto(subClubList);
    }

    @PostMapping(value = "/create")
    public SubClubDTO createSubClub(@RequestBody SubClubDTO subClubDTO){
        if(subClubDTO.upperClub == null){
            Optional<Club> upperClub = clubRepository.findByClubName(subClubDTO.upperClubName);
            if(upperClub.isPresent()){
                subClubDTO.upperClub = upperClub.get();
                SubClub newSubClub = subClubMapper.mapToEntity(subClubDTO);
                SubClub returnedSubClub = subClubService.saveNewSubClub(newSubClub);

                return subClubMapper.mapToDto(returnedSubClub);
            }

        }
        return null;
    }


}
