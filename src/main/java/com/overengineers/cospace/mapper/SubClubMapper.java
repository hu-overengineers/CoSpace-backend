package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.SubClub;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubClubMapper {


    SubClubDTO mapToDto(SubClub subClub);

    SubClub mapToEntity(SubClubDTO subClubDTO);

    List<SubClubDTO> mapToDto(List<SubClub> subClubList);

    List<SubClub> mapToEntity(List<SubClubDTO> subClubDTOList);
}
