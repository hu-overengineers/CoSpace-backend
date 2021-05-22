package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.*;
import com.overengineers.cospace.entity.*;
import com.overengineers.cospace.mapper.*;
import com.overengineers.cospace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModeratorService {

    private final MemberRepository memberRepository;
    private final SubClubRepository subClubRepository;

    private final SecurityService securityService;

    private final BanRepository banRepository;

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    private final ReportRepository reportRepository;

    private final EnrollmentRepository enrollmentRepository;

    private final int maxBanCountForDismiss = 3;
    private final int regularBanDurationAsDay = 5;

    public boolean moderatorCheckBySubClubName(String subClubName){
        SubClub subClub = securityService.getSubClubOfModerator();
        if(subClub == null)
            return false; // Member is not a moderator
        if(subClub.getName() != subClubName)
            return false; // Member is a moderator of another SubClub
        return true;
    }
    @Transactional
    public Ban banMemberFromSubClub(String username, String subClubName, String reason) {
        if(!moderatorCheckBySubClubName(subClubName))
            return null; // Authorized member is not the moderator of the SubClub

        if(!securityService.isAuthorizedToSubClub(username, subClubName)){
            return null; // Not authorized already ( banned or not enrolled )
        }

        Member member = memberRepository.findByUsername(username);
        if (member == null)
            return null; // User is not found

        Optional<Ban> optionalBan = banRepository.findBySubClubNameAndMember_Username(subClubName, username);
        Date expirationDate = UtilService.calculateDate(regularBanDurationAsDay);
        Ban newBan;

        if(!optionalBan.isPresent()){ // No previous ban
            Ban ban = new Ban(reason, expirationDate, 1, false, subClubName, member);
            newBan = banRepository.save(ban);
            return newBan;
        } else {
            Ban ban = optionalBan.get();
            ban.setCount(ban.getCount() + 1);
            ban.setEndDate(expirationDate);
            ban.setReason(reason);
            newBan = banRepository.save(ban);
        }

        if(optionalBan.get().getCount() >= maxBanCountForDismiss) { // Previous ban count is more than max count
            SubClub subClub = subClubRepository.findByName(subClubName).get();
            subClub.getDismissibleMembers().add(member); // Add to dismissible list of the SubClub for moderator
            subClubRepository.save(subClub);
        }

        return newBan;
    }

    @Transactional
    public EventDTO createEvent(EventDTO eventDTO) {

        SubClub subClub = securityService.getAuthorizedMember().getModeratorSubClub();

        if(subClub == null){
            return null;
        }
        if(!moderatorCheckBySubClubName(subClub.getName()))
            return null; // Authorized member is not the moderator of the SubClub

        Event newEvent = eventMapper.mapToEntity(eventDTO);
        newEvent.setOnline(Boolean.parseBoolean(eventDTO.getIsOnline()));
        newEvent.setParent(subClub);
        Event savedEvent = eventRepository.save(newEvent);
        return eventMapper.mapToDto(savedEvent);

    }


    @Transactional
    // deleteEvent returns true if event is deleted successfully
    public boolean deleteEvent(EventDTO eventDTO){

        String subClubName = eventDTO.getParentName();
        if(!moderatorCheckBySubClubName(subClubName))
            return false; // Authorized member is not the moderator of the SubClub

        Optional<Event> optionalEvent = eventRepository.findById(eventDTO.getId());

        if(!optionalEvent.isPresent()){
            return false;
        } else{
            eventRepository.deleteById(eventDTO.getId());
            return true;
        }

    }

    @Transactional
    public EventDTO updateEvent(EventDTO eventDTO){

        String subClubName = eventDTO.getParentName();
        if(!moderatorCheckBySubClubName(subClubName))
            return null; // Authorized member is not the moderator of the SubClub

        Optional<Event> optionalEvent = eventRepository.findById(eventDTO.getId());

        if(!optionalEvent.isPresent())
            return null;

        Event event = optionalEvent.get();

        event.setDetails(eventDTO.getDetails());
        event.setLocation(eventDTO.getLocation());
        event.setOnline(Boolean.parseBoolean(eventDTO.getIsOnline()));
        event.setTitle(eventDTO.getTitle());
        event.setUtilLink(eventDTO.getUtilLink());
        Event updatedEvent = eventRepository.save(event);
        return eventMapper.mapToDto(updatedEvent);

    }

    public List<Member> getDismissibleList() {
        SubClub subClub = securityService.getSubClubOfModerator();
        if (subClub == null)
            return null;

        return new ArrayList<>(subClub.getDismissibleMembers());
    }

    public List<Report> getReports() {
        SubClub subClub = securityService.getSubClubOfModerator();
        if (subClub == null)
            return null;

        return reportRepository.findByPost_Parent_Name(subClub.getName());
    }

    public Enrollment dismissMemberFromSubClub(String username)
    {
        String subClubName = securityService.getAuthorizedMember().getModeratorSubClub().getName();
        if(!moderatorCheckBySubClubName(subClubName))
            return null; // Authorized member is not the moderator of the SubClub

        Enrollment memberEnrollment = enrollmentRepository.findByMember_UsernameAndSubClub_Name(username, subClubName);
        if (memberEnrollment == null)
            return null;

        if(!memberEnrollment.isEnrolled())
            return null; // already dismissed

        for(Member member : getDismissibleList()){
            if (member.getUsername().equals(username)){
                memberEnrollment.setEnrolled(false);
                Enrollment savedEnrollment = enrollmentRepository.save(memberEnrollment);
                return savedEnrollment;
            }
        }

        return null; // Not in dismissible list
    }

}
