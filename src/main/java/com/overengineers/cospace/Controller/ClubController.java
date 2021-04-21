package com.overengineers.cospace.Controller;

import com.overengineers.cospace.Dto.ClubDTO;
import com.overengineers.cospace.Entity.Club;
import com.overengineers.cospace.Mapper.ClubMapper;
import com.overengineers.cospace.Service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubController {
    private final ClubService clubService;
    private final ClubMapper clubMapper;


    @GetMapping(value = "/all-clubs")
    public List<ClubDTO> listAllEvents() {
        List<Club> clubList = clubService.listAllClubs();
        return clubMapper.mapToDto(clubList);
    }
}
