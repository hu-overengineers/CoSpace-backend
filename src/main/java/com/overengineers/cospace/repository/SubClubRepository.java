package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.SubClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubClubRepository extends JpaRepository<SubClub, Long> {
    Optional<SubClub> findBySubClubName(String subClubName);
}
