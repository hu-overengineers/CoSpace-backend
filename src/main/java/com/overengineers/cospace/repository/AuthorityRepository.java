package com.overengineers.cospace.repository;

import com.overengineers.cospace.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    @Transactional Authority save(Authority authority);
    @Transactional Authority findByAuthority(String authority);
}
