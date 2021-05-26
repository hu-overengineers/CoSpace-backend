package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.SubClubCreateRequestDTO;
import com.overengineers.cospace.entity.Event;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.entity.SubClubCreateRequest;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.repository.SubClubCreateRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SubClubCreateRequestRepository subClubCreateRequestRepository;

    private final SecurityService securityService;
    private final PrivateMessagingService privateMessagingService;
    private final EnrollmentService enrollmentService;
    private final SearchService searchService;

    public Member getByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    private final SubClubService subClubService;

    public List<SubClub> getEnrolledSubClubs() {
        try {
            String authorizedMemberUsername = securityService.getAuthorizedUsername();
            List<SubClub> subClubs = enrollmentService.getMemberSubClubs(authorizedMemberUsername);
            return subClubs;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Event> getAttendedEvents() {
        try {
            return new ArrayList<>(securityService.getAuthorizedMember().getAttendedEvents());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Member> searchWithinTheSameSubClub(String query, String subClubName, Pageable pageable) {
        if (!securityService.isAuthorizedToSubClub(subClubName))
            return null;

        // Sort by username, alphabetically
        UtilService.fixPageableSort(pageable, "username", true);
        return searchService.searchMembersWithinTheSameSubClub(query, subClubName, pageable).toList();
    }

    public SubClubCreateRequest requestSubClub(SubClubCreateRequestDTO subClubCreateRequestDTO) {
        String subClubName = subClubCreateRequestDTO.getSubClubName();
        String clubName = subClubCreateRequestDTO.getClubName();
        String username = securityService.getAuthorizedUsername();
        SubClubCreateRequest subClubCreateRequest = new SubClubCreateRequest(subClubName, clubName, username);
        SubClubCreateRequest savedRequest = subClubCreateRequestRepository.save(subClubCreateRequest);
        return savedRequest;
    }

    public Member getMemberByName(String username) {
        return memberRepository.findByUsername(username);
    }
}
