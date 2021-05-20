package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAll();
    List<Report> findByPost_Parent_Name(String name); // findBySubClubName
}
