package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
