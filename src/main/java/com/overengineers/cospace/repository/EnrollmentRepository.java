package com.overengineers.cospace.repository;

import com.overengineers.cospace.dto.EnrollmentDTO;
import com.overengineers.cospace.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<EnrollmentDTO> findBySubClub_Name(String name);
    List<EnrollmentDTO> findByMember_Username(String username);

}
