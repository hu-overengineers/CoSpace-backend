package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubClubService {

    private final SubClubRepository subClubRepository;
    private final SubClubMapper subClubMapper;

    public List<SubClubDTO> listAllSubClubs(){
        List<SubClub> subClubList= subClubRepository.findAll();
        List<SubClubDTO> subClubDTOList = subClubMapper.mapToDto(subClubList);
        return subClubDTOList;
    }

    public SubClub saveNewSubClub(SubClub subClub){ return subClubRepository.save(subClub); }

    public SubClubDTO rate(String subClubName) {
        Optional<SubClub> optionalSubClub = subClubRepository.findByName(subClubName);
        if(!optionalSubClub.isPresent()){
            return null;
        }
        else{
            SubClub currentSubClub = optionalSubClub.get();
            int currentRating = currentSubClub.getRating();
            currentSubClub.setRating(currentRating + 1);
            subClubRepository.save(currentSubClub);
            return subClubMapper.mapToDto(currentSubClub);
        }
    }
}
