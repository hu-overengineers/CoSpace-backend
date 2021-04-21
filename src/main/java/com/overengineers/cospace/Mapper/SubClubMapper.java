package com.overengineers.cospace.Mapper;

import com.overengineers.cospace.Dto.ClubDTO;
import com.overengineers.cospace.Dto.SubClubDTO;
import com.overengineers.cospace.Entity.SubClub;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubClubMapper {


    SubClubDTO mapToDto(SubClub subClub);

    SubClub mapToEntity(SubClubDTO subClubDTO);

    List<SubClubDTO> mapToDto(List<SubClub> subClubList);

    List<SubClub> mapToEntity(List<SubClubDTO> subClubDTOList);
}
