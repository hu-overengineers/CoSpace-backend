package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.ClubDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.mapper.ClubMapper;
import com.overengineers.cospace.repository.ClubRepository;
import com.overengineers.cospace.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/club")
public class ClubController {
    private final ClubService clubService;

    @GetMapping(value = "/all")
    public List<ClubDTO> listAllClubs() {
        return clubService.listAllClubs();
    }

}
