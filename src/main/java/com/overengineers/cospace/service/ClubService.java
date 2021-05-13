package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.mapper.ClubMapper;
import com.overengineers.cospace.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;

    public List<ClubDTO> listAllClubs(){
        List<Club> clubList = clubRepository.findAll();
        List<ClubDTO> clubDTOList = clubMapper.mapToDto(clubList);
        return clubDTOList;
    }

    public Club saveNewClub(Club club){
        return clubRepository.save(club); }
}
