package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.mapper.ClubMapper;
import com.overengineers.cospace.repository.ClubRepository;
import com.overengineers.cospace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;

    private final MemberRepository memberRepository;

    public List<ClubDTO> listAllClubs(){
        List<Club> clubList = clubRepository.findAll();
        List<ClubDTO> clubDTOList = clubMapper.mapToDto(clubList);
        return clubDTOList;
    }

    public Optional<Club> findByClubName(String clubName){
        return clubRepository.findByClubName(clubName);
    }

    public Club saveNewClub(Club club){
        return clubRepository.save(club); }

    public ResponseEntity<String> enrollClub(String clubName){
        if (findByClubName(clubName).isPresent()){
            Club currentClub = findByClubName(clubName).get();
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Member currentMember = memberRepository.findByUsername(username);
            currentMember.getClubs().add(currentClub);
            currentClub.getMembers().add(currentMember);
            saveNewClub(currentClub);
            memberRepository.save(currentMember);
            return ResponseEntity.ok("You are successfully enrolled.");
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404
                .body("SUBCLUB NOT FOUND!");

    }
}
