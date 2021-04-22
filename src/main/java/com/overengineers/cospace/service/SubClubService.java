package com.overengineers.cospace.service;

import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.repository.SubClubRepository;
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
