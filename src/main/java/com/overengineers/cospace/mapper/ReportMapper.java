package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.ReportDTO;
import com.overengineers.cospace.entity.Report;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    ReportDTO mapToDto(Report report);

    Report mapToEntity(ReportDTO reportDTO);

    List<ReportDTO> mapToDto(List<Report> reportList);

    List<Report> mapToEntity(List<ReportDTO> reportDTOList);
}
