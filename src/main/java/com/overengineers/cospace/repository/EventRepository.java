package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByParent_NameAndDateAfter(String parentName, Date date);

}
