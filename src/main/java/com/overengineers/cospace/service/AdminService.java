package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.dto.ReportDTO;
import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.ClubMapper;
import com.overengineers.cospace.mapper.ReportMapper;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.repository.ClubRepository;
import com.overengineers.cospace.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final ClubService clubService;
    private final ClubMapper clubMapper;
    private final ClubRepository clubRepository;

    private final SubClubMapper subClubMapper;
    private final SubClubService subClubService;

    private final ReportMapper reportMapper;
    private final ReportRepository reportRepository;

    public ClubDTO createClub(ClubDTO clubDTO) {
        Club club = clubMapper.mapToEntity(clubDTO);
        Club newClub = clubService.saveNewClub(club);
        return clubMapper.mapToDto(newClub);
    }

    public SubClubDTO createSubClub(SubClubDTO subClubDTO) {
        if(subClubDTO.upperClub == null){
            Optional<Club> upperClub = clubRepository.findByClubName(subClubDTO.upperClubName);
            if(upperClub.isPresent()){
                subClubDTO.upperClub = upperClub.get();
                SubClub newSubClub = subClubMapper.mapToEntity(subClubDTO);
                SubClub returnedSubClub = subClubService.saveNewSubClub(newSubClub);
                return subClubMapper.mapToDto(returnedSubClub);
            }
        }
        return null;
    }

    public List<ReportDTO> getAllReports() {
        return reportMapper.mapToDto(reportRepository.findAll());
    }
}
