package com.overengineers.cospace.service;

import com.overengineers.cospace.entity.Post;
import com.overengineers.cospace.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> listAllPosts(){
        return postRepository.findAll();
    }

    public List<Post> listSubClubPosts(String subClubName){
        return postRepository.findByPostSubClubName(subClubName);
    }

    public List<Post> listAuthorPosts(String postAuthor){
        return postRepository.findByPostAuthor(postAuthor);
    }

    public Post saveNewPost(Post post){ return postRepository.save(post); }
}
