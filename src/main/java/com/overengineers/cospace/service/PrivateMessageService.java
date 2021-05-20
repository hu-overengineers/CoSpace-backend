package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.PrivateMessageDTO;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.PrivateMessage;
import com.overengineers.cospace.mapper.PrivateMessageMapper;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.repository.PrivateMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateMessageService {

    private final PrivateMessageRepository privateMessageRepository;
    private final PrivateMessageMapper privateMessageMapper;

    private final MemberRepository memberRepository;

    private final SecurityService securityService;

    public PrivateMessageDTO send(PrivateMessageDTO privateMessageDTO) {
        PrivateMessage privateMessage = privateMessageMapper.mapToEntity(privateMessageDTO);
        Member targetMember = memberRepository.findByUsername(privateMessageDTO.getTargetMemberUsername());
        privateMessage.setAuthor(securityService.getAuthorizedUsername());
        privateMessage.setTargetMember(targetMember);
        PrivateMessage savedPrivateMessage = privateMessageRepository.save(privateMessage);
        return privateMessageMapper.mapToDto(savedPrivateMessage);
    }

    public List<PrivateMessageDTO> getAllByAuthorizedMember() {
        String username = securityService.getAuthorizedUsername();
        return privateMessageMapper.mapToDto(privateMessageRepository.findByTargetMember_Username(username));
    }


}
