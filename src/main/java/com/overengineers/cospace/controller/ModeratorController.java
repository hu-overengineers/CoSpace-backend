package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.BanDTO;
import com.overengineers.cospace.dto.EventDTO;
import com.overengineers.cospace.service.ModeratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/moderator")
public class ModeratorController {

    private final ModeratorService moderatorService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/ban")
    public BanDTO banMemberFromSubClub(@RequestParam(name = "username") String username,
                                       @RequestParam(name = "subClubName") String subClubName,
                                       @RequestParam(name = "reason") String reason){
        return moderatorService.banMemberFromSubClub(username, subClubName, reason);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/create-event")
    public EventDTO createEvent(@RequestBody EventDTO eventDTO){
        return moderatorService.createEvent(eventDTO);
    }





    // get dismissible list

    // dismiss from SubClub

    // create event

    // update event

    // delete event

    // getReportsFor only mod's subclub

    // member participate-unparticipate event in subclub controller

    // postservice: savepost->createpost


}
