package com.overengineers.cospace.Service;

import com.overengineers.cospace.Entity.Club;
import com.overengineers.cospace.Mapper.ClubMapper;
import com.overengineers.cospace.Repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    @Autowired
    public ClubService(ClubRepository clubRepository, ClubMapper clubMapper) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
    }

    public List<Club> listAllClubs(){
        return clubRepository.findAll();
    }

    public Club saveNewClub(Club club){ return clubRepository.save(club); }
}
