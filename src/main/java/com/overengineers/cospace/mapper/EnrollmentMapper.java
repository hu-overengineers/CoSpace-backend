package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.EnrollmentDTO;
import com.overengineers.cospace.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(source = "enrollment", target = "memberUsername", qualifiedByName = "memberUsername")
    @Mapping(source = "enrollment", target = "subClubName", qualifiedByName = "subClubName")
    EnrollmentDTO mapToDto(Enrollment enrollment);

    @Named("memberUsername")
    default String memberToMemberUsername(Enrollment enrollment){
        if(enrollment.getMember() != null)
            return enrollment.getMember().getUsername();
        else
            return "";
    }

    @Named("subClubName")
    default String subClubToSubClubName(Enrollment enrollment){
        if(enrollment.getSubClub() != null)
            return enrollment.getSubClub().getName();
        else
            return "";
    }

    Enrollment mapToEntity(EnrollmentDTO enrollmentDTO);

    List<EnrollmentDTO> mapToDto(List<Enrollment> enrollmentList);

    List<Enrollment> mapToEntity(List<EnrollmentDTO> enrollmentDTOList);

}
