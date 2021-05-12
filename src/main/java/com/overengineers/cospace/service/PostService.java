package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.PostDTO;
import com.overengineers.cospace.entity.Post;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.PostMapper;
import com.overengineers.cospace.repository.PostRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final SubClubRepository subClubRepository;

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public List<Post> listAllPosts(){
        return postRepository.findAll();
    }

    public List<Post> listSubClubPosts(String subClubName){
        return postRepository.findByPostSubClubName(subClubName);
    }

    public List<Post> listAuthorPosts(String postAuthor){
        return postRepository.findByPostAuthor(postAuthor);
    }

    public PostDTO savePost(PostDTO postDTO) {
        if(postDTO.postSubClub == null){
            Optional<SubClub> postSubClub = subClubRepository.findBySubClubName(postDTO.postSubClubName);
            if(postSubClub.isPresent()){
                postDTO.postSubClub = postSubClub.get();
                Post newPost = postMapper.mapToEntity(postDTO);
                newPost.setPostVoting(0);
                Post savedPost = postRepository.save(newPost);
                return postMapper.mapToDto(savedPost);
            }
        }
        return null;
    }

    public List<PostDTO> getSubClubPosts(String subClubName) {
        return postMapper.mapToDto(postRepository.findByPostSubClubName(subClubName));
    }
}
