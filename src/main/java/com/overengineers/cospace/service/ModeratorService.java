package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.BanDTO;
import com.overengineers.cospace.dto.EventDTO;
import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.entity.*;
import com.overengineers.cospace.mapper.BanMapper;
import com.overengineers.cospace.mapper.EventMapper;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.repository.BanRepository;
import com.overengineers.cospace.repository.EventRepository;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModeratorService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    private final SubClubRepository subClubRepository;

    private final SecurityService securityService;

    private final BanRepository banRepository;
    private final BanMapper banMapper;

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    private final int maxBanCountForDismiss = 3;
    private final int regularBanDurationAsDay = 5;

    @Transactional
    public BanDTO banMemberFromSubClub(String username, String subClubName, String reason) {
        if(!securityService.isModeratorOfSubClub(subClubName))
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
            return banMapper.mapToDto(newBan);
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

        return banMapper.mapToDto(newBan);
    }

    @Transactional
    public EventDTO createEvent(EventDTO eventDTO) {

        String subClubName = eventDTO.getParentName();
        if(!securityService.isModeratorOfSubClub(subClubName))
            return null; // Authorized member is not the moderator of the SubClub

        SubClub subClub  = subClubRepository.findByName(subClubName).get();
        Event newEvent = eventMapper.mapToEntity(eventDTO);
        newEvent.setParent(subClub);
        Event savedEvent = eventRepository.save(newEvent);
        return eventMapper.mapToDto(savedEvent);

    }


    @Transactional
    // deleteEvent returns true if event is deleted successfully
    public boolean deleteEvent(EventDTO eventDTO){

        String subClubName = eventDTO.getParentName();
        if(!securityService.isModeratorOfSubClub(subClubName))
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
        if(!securityService.isModeratorOfSubClub(subClubName))
            return null; // Authorized member is not the moderator of the SubClub

        Optional<Event> optionalEvent = eventRepository.findById(eventDTO.getId());

        if(!optionalEvent.isPresent())
            return null;

        Event event = optionalEvent.get();

        event.setDetails(eventDTO.getDetails());
        event.setLocation(eventDTO.getLocation());
        event.setOnline(eventDTO.isOnline());
        event.setTitle(eventDTO.getTitle());
        event.setUtilLink(eventDTO.getUtilLink());
        Event updatedEvent = eventRepository.save(event);
        return eventMapper.mapToDto(updatedEvent);

    }

    public List<MemberDTO> getDismissibleList() {
        Member moderator = securityService.getAuthorizedMember();
        SubClub subClub = moderator.getModeratorSubClub();

        return memberMapper.mapToDto(new ArrayList<>(subClub.getDismissibleMembers()));
    }


    // get dismissible list

    // dismiss from subclub

    // getReports for only mod's subclub
}
