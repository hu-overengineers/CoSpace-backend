package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);
    Member findByEmail(String email);
    List<Member> findByEnrollments_SubClub_NameAndEnrollments_IsEnrolledTrue(String subClubName);
    void deleteByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Search
    List<Member> findByUsernameIgnoreCaseContaining(String username, Pageable page);
    List<Member> findByUsernameIgnoreCaseContainingAndEnrollments_SubClub_Name(String username, String name, Pageable page);

}
