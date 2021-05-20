package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.PrivateMessageDTO;
import com.overengineers.cospace.entity.PrivateMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrivateMessageMapper {

    @Mapping(source = "privateMessage", target = "targetMemberUsername", qualifiedByName = "targetMemberUsername")
    PrivateMessageDTO mapToDto(PrivateMessage privateMessage);

    @Named("targetMemberUsername")
    default String targetMemberToTargetMemberUsername(PrivateMessage privateMessage){
        if(privateMessage.getTargetMember() != null)
            return privateMessage.getTargetMember().getUsername();
        else
            return "";
    }

    PrivateMessage mapToEntity(PrivateMessageDTO privateMessageDTO);

    List<PrivateMessageDTO> mapToDto(List<PrivateMessage> privateMessageList);

    List<PrivateMessage> mapToEntity(List<PrivateMessageDTO> privateMessageDTOList);

}
