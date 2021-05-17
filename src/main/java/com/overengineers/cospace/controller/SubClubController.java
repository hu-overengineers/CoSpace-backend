package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.ReviewDTO;
import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.service.SubClubService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/subclub")
public class SubClubController {
    private final SubClubService subClubService;

    @GetMapping(value = "/all")
    public List<SubClubDTO> listAllSubClubs() {
        return subClubService.listAllSubClubs();
    }

    @PostMapping("/review")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ReviewDTO review(@RequestBody ReviewDTO reviewDTO){
        return subClubService.review(reviewDTO); // TODO: Auth check by enrollment
    }

    @GetMapping("/reviews")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<ReviewDTO> getReviewsByParentName(@RequestParam(name = "subClubName") String subClubName){
        return subClubService.getReviewsByParentName(subClubName);
    }

    @GetMapping("/search")
    public List<SubClubDTO> search(@RequestParam(name = "query") String query, Pageable pageable){
        return subClubService.search(query,  pageable);
    }

}
