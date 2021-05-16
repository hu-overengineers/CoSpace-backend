package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByParentName(String subClubName);
}
