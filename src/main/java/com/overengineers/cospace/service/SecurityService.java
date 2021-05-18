package com.overengineers.cospace.service;

import com.overengineers.cospace.entity.Ban;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.repository.BanRepository;
import com.overengineers.cospace.repository.ClubRepository;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final MemberRepository memberRepository;
    private final SubClubRepository subClubRepository;
    private final BanRepository banRepository;

    public String getAuthorizedUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }

    public Member getAuthorizedMember(){
        String username = getAuthorizedUsername();
        return memberRepository.findByUsername(username);
    }

    public boolean isMemberBannedFromSubClub(String subClubName){
        if(!subClubRepository.findByName(subClubName).isPresent())
            return true; // SubClub not found

        String username = getAuthorizedUsername();
        Optional<Ban> optionalBan = banRepository.findBySubClubNameAndMember_Username(subClubName, username);

        if(!optionalBan.isPresent()){
            return false; // No ban
        }
        else{
            Ban ban = optionalBan.get();
            Date now = Calendar.getInstance().getTime();
            if(ban.getEndDate().after(now)){
                return true; // Ban continue
            }
            else{
                return false; // Previous ban exist, but finished
            }
        }
    }

    public boolean isMemberEnrolledToSubClub(String subClubName){
        Set<SubClub> userSubClubs = getAuthorizedMember().getSubClubs();

        if (!subClubRepository.findByName(subClubName).isPresent()) // SubClub is not found
            return false;

        SubClub subClub = subClubRepository.findByName(subClubName).get();

        return userSubClubs.contains(subClub);
    }

    public boolean isAuthorizedToSubClub(String subClubName){
        return !isMemberBannedFromSubClub(subClubName) && isMemberEnrolledToSubClub(subClubName);
    }
}
