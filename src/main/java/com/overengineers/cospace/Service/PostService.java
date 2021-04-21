package com.overengineers.cospace.Service;

import com.overengineers.cospace.Entity.Post;
import com.overengineers.cospace.Repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
