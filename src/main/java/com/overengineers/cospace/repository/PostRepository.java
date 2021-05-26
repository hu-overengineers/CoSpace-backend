package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByParentNameAndCreatedBetween(String parentName, Date start, Date end, Pageable pageable);

    Page<Post> findByParent_Parent_NameAndCreatedBetween(String name, Date start, Date end, Pageable pageable);

    Page<Post> findByCreatedBetween(Date start, Date end, Pageable pageable);

    Page<Post> findByContentIgnoreCaseContaining(String query, Pageable pageable);

    List<Post> findByAuthor(String author);

    List<Post> findByAuthorAndParentName(String author, String parentName);

    Post findById(String id);

    Integer countAllByParentNameAndCreatedBetween(String parentName, Date start, Date end);

}
