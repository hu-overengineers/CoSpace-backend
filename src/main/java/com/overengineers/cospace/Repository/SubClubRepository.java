package com.overengineers.cospace.Repository;

import com.overengineers.cospace.Entity.Member;
import com.overengineers.cospace.Entity.SubClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubClubRepository extends JpaRepository<SubClub, Long> {
    Optional<SubClub> findBySubClubName(String subClubName);

}
