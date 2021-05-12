package com.overengineers.cospace.util;

import com.overengineers.cospace.entity.Authority;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.repository.AuthorityRepository;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.service.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DatabasePopulate {

    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public void populateDatabase(){
        List<Authority> savedAuthorities = authorityRepository.saveAll(Set.of(new Authority(null, null, "USER"), new Authority(null, null,"ADMIN")));
        Member member = new Member("yusuf", passwordEncoder.encode("12345"),"ketenyusuf@gmail.com", null, null,Set.of(savedAuthorities.get(0), savedAuthorities.get(1)));
        memberRepository.save(member);
    }
}
