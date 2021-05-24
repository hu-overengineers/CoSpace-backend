package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.ClubMapper;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.repository.ClubRepository;
import com.overengineers.cospace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

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


    @Transactional
    public Club saveNewClub(Club club){
        return clubRepository.save(club); }


    public List<ClubDTO> search(String query, Pageable pageable){
        // Sort by name, alphabetic order
        Pageable newPageable = UtilService.fixPageableSort(pageable, "name", true);
        return clubMapper.mapToDto(clubRepository.findByNameIgnoreCaseContaining(query, newPageable));
    }

    public Club getByName(String clubName) {
        return clubRepository.findByName(clubName).orElse(null);
    }

}
