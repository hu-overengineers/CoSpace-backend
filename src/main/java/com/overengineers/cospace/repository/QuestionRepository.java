package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByParent_Name(String subClubName);

}
