package com.overengineers.cospace.Repository;

import com.overengineers.cospace.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPostSubClubName(String postSubClubName);
    List<Post> findByPostAuthor(String postAuthor);
}
