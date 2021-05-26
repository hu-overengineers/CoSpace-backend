package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.SubClubCreateRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubClubCreateRequestRepository extends JpaRepository<SubClubCreateRequest, Long> {

    List<SubClubCreateRequest> findByClubName(String clubName);
    void deleteBySubClubName(String subClubName);
    long countAllByClubName(String clubName);

}
