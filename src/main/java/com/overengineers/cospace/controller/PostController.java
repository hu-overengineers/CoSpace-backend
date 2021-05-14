package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.PostDTO;
import com.overengineers.cospace.dto.ReportDTO;
import com.overengineers.cospace.service.PostService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/create")
    public PostDTO createPost(@RequestBody PostDTO postDTO){
        return postService.savePost(postDTO);
    }

    @GetMapping(value = "/{subClubName}")
    @ResponseBody
    public List<PostDTO> getSubClubPosts(@PathVariable String subClubName){
        return postService.getSubClubPosts(subClubName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/report")
    public ReportDTO reportPost(@RequestBody ReportDTO reportDTO){
        return postService.reportPost(reportDTO);
    }



}
