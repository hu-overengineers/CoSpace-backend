package com.overengineers.cospace.Mapper;

import org.mapstruct.Mapper;
import com.overengineers.cospace.Dto.ClubDTO;
import com.overengineers.cospace.Entity.Club;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ClubMapper {


    ClubDTO mapToDto(Club club);

    Club mapToEntity(ClubDTO clubDTO);

    List<ClubDTO> mapToDto(List<Club> clubList);

    List<Club> mapToEntity(List<ClubDTO> clubDTOList);
}
