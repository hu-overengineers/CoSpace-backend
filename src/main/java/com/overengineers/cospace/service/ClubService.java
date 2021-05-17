package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.Member;
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

    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    public List<ClubDTO> listAllClubs(){
        List<Club> clubList = clubRepository.findAll();
        List<ClubDTO> clubDTOList = clubMapper.mapToDto(clubList);
        return clubDTOList;
    }

    public Optional<Club> findByClubName(String clubName){
        return clubRepository.findByName(clubName);
    }

    @Transactional
    public Club saveNewClub(Club club){
        return clubRepository.save(club); }

    @Transactional
    public ResponseEntity<String> enrollClub(String clubName){
        if (findByClubName(clubName).isPresent()){
            Club currentClub = findByClubName(clubName).get();
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Member currentMember = memberRepository.findByUsername(username);
            currentMember.getClubs().add(currentClub);
            memberRepository.save(currentMember);
            return ResponseEntity.ok("You are successfully enrolled.");
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404
                .body("CLUB NOT FOUND!");
    }

    public List<ClubDTO> search(String query, Pageable pageable){
        return clubMapper.mapToDto(clubRepository.findByNameIgnoreCaseContaining(query, pageable));
    }

    public List<MemberDTO> getClubMembers(String clubName) {
        if(!findByClubName(clubName).isPresent()) {
            return null;
        }

        Club club = findByClubName(clubName).get();
        return memberMapper.mapToDto(new ArrayList<>(club.getMembers()));
    }
}
