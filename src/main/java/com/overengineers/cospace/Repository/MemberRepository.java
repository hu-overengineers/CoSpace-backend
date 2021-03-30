package com.overengineers.cospace.Repository;

import com.overengineers.cospace.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
