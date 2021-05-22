package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.PrivateMessageDTO;
import com.overengineers.cospace.entity.PrivateMessage;
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

    private final SecurityService securityService;

    @Transactional
    public PrivateMessage send(PrivateMessageDTO privateMessageDTO) {
        PrivateMessage privateMessage = privateMessageMapper.mapToEntity(privateMessageDTO);
        privateMessage.setSenderUsername(securityService.getAuthorizedUsername());
        return privateMessageRepository.save(privateMessage);
    }

    public List<PrivateMessage> getAllMessagesOfAuthorizedMember() {
        String username = securityService.getAuthorizedUsername();
        return privateMessageRepository.findByReceiverUsernameOrSenderUsername(username, username);
    }


}
