package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.SubClub;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubClubMapper {

    @Mapping(source = "subClub", target = "parentName", qualifiedByName = "parentName")
    SubClubDTO mapToDto(SubClub subClub);

    @Named("parentName")
    default String parentToParentName(SubClub subClub){
        return subClub.getParent().getName();
    }

    SubClub mapToEntity(SubClubDTO subClubDTO);

    List<SubClubDTO> mapToDto(List<SubClub> subClubList);

    List<SubClub> mapToEntity(List<SubClubDTO> subClubDTOList);
}
