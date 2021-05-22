package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.SubClub;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubClubRepository extends JpaRepository<SubClub, Long> {
    Optional<SubClub> findByName(String name);
    List<SubClub> findByNameIgnoreCaseContaining(String name, Pageable pageable);
    List<SubClub> findByParent_Name(String name);
    List<SubClub> findByEnrollmentsMemberUsernameAndEnrollmentsIsEnrolledTrue(String username);
}
