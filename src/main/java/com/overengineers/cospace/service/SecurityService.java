package com.overengineers.cospace.service;

import com.overengineers.cospace.entity.Ban;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.repository.BanRepository;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final BanRepository banRepository;
    private final MemberRepository memberRepository;
    private final SubClubRepository subClubRepository;

    private final EnrollmentService enrollmentService;

    public String getAuthorizedUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }

    public Member getAuthorizedMember(){
        String username = getAuthorizedUsername();
        return memberRepository.findByUsername(username);
    }

    public boolean isMemberBannedFromSubClub(String username, String subClubName){
        if(!subClubRepository.findByName(subClubName).isPresent()){
            System.out.println("isMemberBannedFromSubClub() -> SubClub: " + subClubName + " is not found");
            return true;
        }


        Optional<Ban> optionalBan = banRepository.findBySubClubNameAndMember_Username(subClubName, username);

        if(!optionalBan.isPresent()){
            return false; // No ban
        }
        else{
            Ban ban = optionalBan.get();
            Date now =UtilService.now();
            if(ban.getEndDate().after(now)){
                return true; // Ban continue
            }
            else{
                return false; // Previous ban exist, but finished
            }
        }
    }

    public boolean isMemberBannedFromSubClub(String subClubName){
        String username = getAuthorizedUsername();
        return isMemberBannedFromSubClub(username, subClubName);
    }

    public boolean isMemberEnrolledToSubClub(String username, String subClubName) {
        List<SubClub> userSubClubs = enrollmentService.getMemberSubClubs(username);
        SubClub subClub = subClubRepository.findByName(subClubName).get();

        if (subClub == null){
            System.out.println("isMemberEnrolledToSubClub() -> SubClub: " + subClubName + " is not found");
            return false;
        }
        else{
            return userSubClubs.contains(subClub);
        }
    }

    public boolean isMemberEnrolledToSubClub(String subClubName) {
        String username = getAuthorizedUsername();
        return isMemberEnrolledToSubClub(username, subClubName);
    }

    public boolean isAuthorizedToSubClub(String username, String subClubName) {
        return !isMemberBannedFromSubClub(username, subClubName) && isMemberEnrolledToSubClub(username, subClubName);
    }

    public boolean isAuthorizedToSubClub(String subClubName){
        String username = getAuthorizedUsername();
        return isAuthorizedToSubClub(username, subClubName);
    }

    public SubClub getSubClubOfModerator(){
        Member moderator =  getAuthorizedMember();
        SubClub subClubOfModerator = moderator.getModeratorSubClub();

        if(subClubOfModerator == null)
            return null;

        String nameSubClubOfModerator = subClubOfModerator.getName();
        SubClub subClubFromRepository = subClubRepository.findByName(nameSubClubOfModerator).get();

        if (subClubFromRepository == null){
            System.out.println("getSubClubOfModerator() -> SubClub:" + nameSubClubOfModerator + " is not found");
            return null;
        }

        if(subClubFromRepository.getModerator() == null)
            return null;

        boolean moderatorCheck = ((subClubFromRepository.getModerator().getUsername().equals(moderator.getUsername())) &&
                (subClubFromRepository.getName().equals(nameSubClubOfModerator)));

        if (moderatorCheck)
            return subClubFromRepository;
        else
            return null;
    }

    public boolean isModBanned(String username){
        List<Ban> banList = banRepository.findByMember_Username(username);

        for (Ban ban: banList) {
            if(ban.isModBan())
                return true; // Banned when the member was a moderator, therefore the member can not be a moderator
        }

        return false; // Never banned or never mod banned
    }

    public boolean isModBanned(){
        String username = getAuthorizedUsername();
        return isModBanned(username);
    }

}
