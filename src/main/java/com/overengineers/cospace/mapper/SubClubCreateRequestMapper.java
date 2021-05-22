package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.SubClubCreateRequestDTO;
import com.overengineers.cospace.entity.SubClubCreateRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubClubCreateRequestMapper {

        SubClubCreateRequestDTO mapToDto(SubClubCreateRequest subClubCreateRequest);

        SubClubCreateRequest mapToEntity(SubClubCreateRequestDTO subClubCreateRequestDTO);

        List<SubClubCreateRequestDTO> mapToDto(List<SubClubCreateRequest> subClubCreateRequestList);

        List<SubClubCreateRequest> mapToEntity(List<SubClubCreateRequestDTO> subClubCreateRequestDTOList);
        
}
