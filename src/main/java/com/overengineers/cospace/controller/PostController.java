package com.overengineers.cospace.controller;

import com.overengineers.cospace.dto.PostDTO;
import com.overengineers.cospace.dto.ReportDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.PostMapper;
import com.overengineers.cospace.repository.SubClubRepository;
import com.overengineers.cospace.service.ClubService;
import com.overengineers.cospace.service.PostService;

import com.overengineers.cospace.service.SubClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    private final PostMapper postMapper;

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
    @GetMapping(value = "/feed")
    @ResponseBody
    public List<PostDTO> getPosts(@RequestParam(name = "name") String feedName,
                                  @RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
                                  @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end,
                                  Pageable pageable) {
        return postMapper.mapToDto(postService.getPosts(feedName, start, end, pageable));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/upvote")
    public PostDTO upvotePost(@RequestParam(name = "postId") Long postId){
        return postService.upvotePost(postId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping(value = "/downvote")
    public PostDTO downvotePost(@RequestParam(name = "postId") Long postId){
        return postService.downvotePost(postId);
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

    @PreAuthorize("permitAll")
    @GetMapping(value = "/by-author")
    public @ResponseBody List<PostDTO> getPostsByAuthorAndSubClub(@RequestParam(name = "username") String username,
                                                                  @Nullable @RequestParam(name = "subClubName") String subClubName) {
        if (subClubName == null || subClubName.isEmpty()) {
            return postService.getPostsByAuthor(username);
        }
        return postService.getPostsByAuthorAndSubClub(username, subClubName);
    }

}
