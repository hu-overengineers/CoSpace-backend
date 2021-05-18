package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.BanDTO;
import com.overengineers.cospace.entity.Ban;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BanMapper {

    @Mapping(source = "ban", target = "memberName", qualifiedByName = "memberName")
    BanDTO mapToDto(Ban ban);

    @Named("memberName")
    default String memberToMemberName(Ban ban){
        if(ban.getMember() != null)
            return ban.getMember().getUsername();
        else
            return "";
    }

    Ban mapToEntity(BanDTO banDTO);

    List<BanDTO> mapToDto(List<Ban> banList);

    List<Ban> mapToEntity(List<BanDTO> banDTOList);
    
}
