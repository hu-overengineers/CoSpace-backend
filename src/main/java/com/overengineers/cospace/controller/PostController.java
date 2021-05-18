package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.PostDTO;
import com.overengineers.cospace.dto.ReportDTO;
import com.overengineers.cospace.service.PostService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*") // TODO: Might need to remove this in production. For debugging purposes.
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PreAuthorize("permitAll")
    @GetMapping
    public PostDTO getPost(@RequestParam(name = "postId") Long postId){
        return postService.getPostDTOById(postId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/create")
    public PostDTO createPost(@RequestBody PostDTO postDTO){
        return postService.createPost(postDTO);
    }

    @PreAuthorize("permitAll")
    @GetMapping(value = "/subClubPosts")
    @ResponseBody
    public List<PostDTO> getSubClubPosts(@RequestParam(name = "subClubName") String subClubName){
        return postService.getSubClubPosts(subClubName);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/vote")
    public PostDTO votePost(@RequestParam(name = "postId") Long postId){
        return postService.votePost(postId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/report")
    public ReportDTO reportPost(@RequestBody ReportDTO reportDTO){
        return postService.reportPost(reportDTO);
    }

    @PreAuthorize("permitAll")
    @GetMapping(value = "/trends")
    public @ResponseBody List<PostDTO> getTrends(Pageable pageable) throws Exception {
        return postService.getTrends(pageable);
    }



}
