package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.PrivateMessageDTO;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.PrivateMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrivateMessageMapper {

    PrivateMessageDTO mapToDto(PrivateMessage privateMessage);

    PrivateMessage mapToEntity(PrivateMessageDTO privateMessageDTO);

    List<PrivateMessageDTO> mapToDto(List<PrivateMessage> privateMessageList);

    List<PrivateMessage> mapToEntity(List<PrivateMessageDTO> privateMessageDTOList);

}
