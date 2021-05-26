package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByName(String name);
    Page<Club> findByNameIgnoreCaseContaining(String name, Pageable pageable);
}
