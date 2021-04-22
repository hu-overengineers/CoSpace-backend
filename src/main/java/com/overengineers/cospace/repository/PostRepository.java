package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPostSubClubName(String postSubClubName);
    List<Post> findByPostAuthor(String postAuthor);
}
