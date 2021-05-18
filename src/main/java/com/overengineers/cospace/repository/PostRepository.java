package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByParentName(String parentName);
    List<Post> findByAuthor(String author);
    Post findById(String id);
    Integer countAllByParentNameAndCreatedBetween(String parentName, LocalDateTime start, LocalDateTime end);
}
