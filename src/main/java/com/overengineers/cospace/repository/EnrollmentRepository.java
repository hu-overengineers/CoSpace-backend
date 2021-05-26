package com.overengineers.cospace.repository;

import com.overengineers.cospace.dto.EnrollmentDTO;
import com.overengineers.cospace.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findBySubClub_Name(String name);
    void deleteBySubClub_Name(String name);
    List<Enrollment> findByMember_Username(String username);
    Enrollment findByMemberUsernameAndSubClubName(String username, String name);
}
