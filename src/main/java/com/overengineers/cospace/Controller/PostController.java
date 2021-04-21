package com.overengineers.cospace.Controller;

import com.overengineers.cospace.Dto.PostDTO;
import com.overengineers.cospace.Entity.Post;
import com.overengineers.cospace.Entity.SubClub;
import com.overengineers.cospace.Mapper.PostMapper;
import com.overengineers.cospace.Repository.PostRepository;
import com.overengineers.cospace.Repository.SubClubRepository;
import com.overengineers.cospace.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final SubClubRepository subClubRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PostService postService;


    @PostMapping(value = "/create")
    public PostDTO createPost(@RequestBody PostDTO postDTO){
        if(postDTO.postSubClub == null){
            Optional<SubClub> postSubClub = subClubRepository.findBySubClubName(postDTO.postSubClubName);
            if(postSubClub.isPresent()){
                postDTO.postSubClub = postSubClub.get();
                Post newPost = postMapper.mapToEntity(postDTO);
                Post savedPost = postService.saveNewPost(newPost);
                return postMapper.mapToDto(savedPost);
            }
        }
        return null;
    }

    @GetMapping(value = "/get")
    @ResponseBody
    public List<PostDTO> getSubClubPosts(@RequestParam("subClubName") String subClubName){
        return postMapper.mapToDto(postRepository.findByPostSubClubName(subClubName));
    }
}
