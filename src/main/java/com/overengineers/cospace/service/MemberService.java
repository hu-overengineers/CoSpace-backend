package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.dto.PrivateMessageDTO;
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
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    private final SubClubMapper subClubMapper;

    private final SecurityService securityService;
    private final PrivateMessageService privateMessageService;

    private final SubClubService subClubService;

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

    public List<MemberDTO> search(String query, String subClubName, Pageable pageable) {
        if(!securityService.isAuthorizedToSubClub(subClubName))
            return null;

        // Sort by username, alphabetically
        UtilService.fixPageableSort(pageable, "username", true);
        return memberMapper.mapToDto(memberRepository.findByUsernameIgnoreCaseContainingAndSubClubs_Name(query, subClubName, pageable));

    }

    public PrivateMessageDTO sendPrivateMessage(PrivateMessageDTO privateMessageDTO) {
        List<SubClubDTO> intersection = subClubService.getCommonSubClubs(securityService.getAuthorizedUsername(), privateMessageDTO.targetMemberUsername);
        if (intersection == null) return null;

        if(intersection.size() > 0) // At least one common SubClub between two members
           return privateMessageService.send(privateMessageDTO);
        else
            return null;
    }

    public List<PrivateMessageDTO> getPrivateMessages(){
        return privateMessageService.getAllByAuthorizedMember();
    }
}
