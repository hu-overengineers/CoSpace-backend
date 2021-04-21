package com.overengineers.cospace.Service;

import com.overengineers.cospace.Entity.SubClub;
import com.overengineers.cospace.Mapper.SubClubMapper;
import com.overengineers.cospace.Repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubClubService {

    private final SubClubRepository subClubRepository;
    private final SubClubMapper subClubMapper;

    public List<SubClub> listAllSubClubs(){
        return subClubRepository.findAll();
    }

    public SubClub saveNewSubClub(SubClub subClub){ return subClubRepository.save(subClub); }
}
