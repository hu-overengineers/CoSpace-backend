package com.overengineers.cospace.Service;

import com.overengineers.cospace.Entity.Club;
import com.overengineers.cospace.Mapper.ClubMapper;
import com.overengineers.cospace.Repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;

    public List<Club> listAllClubs() {
        return clubRepository.findAll();
    }
}
