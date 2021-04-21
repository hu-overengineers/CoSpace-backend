package com.overengineers.cospace.Repository;

import com.overengineers.cospace.Entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByClubName(String clubName);

}
