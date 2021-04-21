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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subclub")
public class SubClubController {
    private final SubClubService subClubService;
    private final SubClubMapper subClubMapper;
    private final SubClubRepository subClubRepository;
    private final ClubRepository clubRepository;

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
