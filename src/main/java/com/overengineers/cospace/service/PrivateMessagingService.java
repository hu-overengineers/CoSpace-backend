package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.PrivateMessageDTO;
import com.overengineers.cospace.entity.PrivateMessage;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.PrivateMessageMapper;
import com.overengineers.cospace.repository.PrivateMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateMessagingService {

    private final PrivateMessageRepository privateMessageRepository;

    private final PrivateMessageMapper privateMessageMapper;

    private final SubClubService subClubService;
    private final SecurityService securityService;
    @Transactional
    public PrivateMessage send(PrivateMessageDTO privateMessageDTO) {
        String currentlySignedInMemberUsername = securityService.getAuthorizedUsername();

        List<SubClub> intersection = subClubService.getCommonSubClubs(currentlySignedInMemberUsername, privateMessageDTO.getReceiverUsername());
        if (intersection == null) return null;

        if(intersection.size() > 0) { // At least one common SubClub between two members
            PrivateMessage privateMessage = privateMessageMapper.mapToEntity(privateMessageDTO);
            privateMessage.setSenderUsername(currentlySignedInMemberUsername);
            return privateMessageRepository.save(privateMessage);
        }
        else {
            return null;
        }
    }

    public List<PrivateMessage> getAllMessagesOfAuthorizedMember() {
        String username = securityService.getAuthorizedUsername();
        return privateMessageRepository.findByReceiverUsernameOrSenderUsername(username, username);
    }


}
