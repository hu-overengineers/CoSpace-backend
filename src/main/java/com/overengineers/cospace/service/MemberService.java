package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.mapper.ClubMapper;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
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

    private final ClubMapper clubMapper;

    public List<ClubDTO> getEnrolledClubs() {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            Set<Club> clubs = memberRepository.findByUsername(username).getClubs();
            return clubMapper.mapToDto(new ArrayList<>(clubs));
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }

    }
}
