package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Ban;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BanRepository extends JpaRepository<Ban, Long> {
    Optional<Ban> findBySubClubNameAndMember_Username(String subClubName, String username);
    List<Ban> findByMember_Username(String username);
}
