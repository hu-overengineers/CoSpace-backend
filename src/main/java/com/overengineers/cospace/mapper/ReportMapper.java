package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.ReportDTO;
import com.overengineers.cospace.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(source = "report", target = "postId", qualifiedByName = "postId")
    ReportDTO mapToDto(Report report);

    @Named("postId")
    default String postToPostId(Report report){
        if (report.getPost() != null)
            return report.getPost().getId().toString();
        else
            return "";
    }

    Report mapToEntity(ReportDTO reportDTO);

    List<ReportDTO> mapToDto(List<Report> reportList);

    List<Report> mapToEntity(List<ReportDTO> reportDTOList);

}
