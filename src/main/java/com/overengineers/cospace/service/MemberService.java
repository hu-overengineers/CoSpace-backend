package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.ClubMapper;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    private final SubClubMapper subClubMapper;

    private final SecurityService securityService;

    public List<SubClubDTO> getEnrolledSubClubs() {
        try{
            Set<SubClub> subClubs = securityService.getAuthorizedMember().getSubClubs();
            return subClubMapper.mapToDto(new ArrayList<>(subClubs));
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }

    }
/*
    public boolean clubAuthCheck(String clubName){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        Set<Club> enrolledClubs = memberRepository.findByUsername(username).getClubs();

        Club result = enrolledClubs.stream()
                .filter(club -> clubName.equals(club.getName()))
                .findAny()
                .orElse(null);

        if(result == null)
            return false;
        return true;


    }
*/
    public List<MemberDTO> search(String query, String subClubName, Pageable pageable) {
        if(!securityService.isAuthorizedToSubClub(subClubName))
            return null;

        return memberMapper.mapToDto(memberRepository.findByUsernameIgnoreCaseContainingAndSubClubs_Name(query, subClubName, pageable));

    }
}
