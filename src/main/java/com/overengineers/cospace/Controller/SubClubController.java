package com.overengineers.cospace.Controller;

import com.overengineers.cospace.Dto.PostDTO;
import com.overengineers.cospace.Dto.SubClubDTO;
import com.overengineers.cospace.Entity.Club;
import com.overengineers.cospace.Entity.Post;
import com.overengineers.cospace.Entity.SubClub;
import com.overengineers.cospace.Mapper.SubClubMapper;
import com.overengineers.cospace.Repository.ClubRepository;
import com.overengineers.cospace.Repository.SubClubRepository;
import com.overengineers.cospace.Service.SubClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

                return  subClubMapper.mapToDto(returnedSubClub);
            }

        }
        return null;
    }


}
