package com.overengineers.cospace.mapper;

import org.mapstruct.Mapper;
import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.entity.Club;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ClubMapper {


    ClubDTO mapToDto(Club club);

    Club mapToEntity(ClubDTO clubDTO);

    List<ClubDTO> mapToDto(List<Club> clubList);

    List<Club> mapToEntity(List<ClubDTO> clubDTOList);
}
