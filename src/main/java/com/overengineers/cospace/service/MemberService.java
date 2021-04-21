package com.overengineers.cospace.service;

import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private MemberRepository memberRepository;
    private MemberMapper memberMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    public List<Member> listAllMembers() {
        return memberRepository.findAll();
    }

    public Member login(String username){
        Optional<Member> optionalMember = memberRepository.findByUsername(username);

        if (optionalMember.isPresent())
        {
            return optionalMember.get();
        }else{
            return null;
        }
    }

    public Member register(Member member){
        if (memberRepository.findByUsername(member.getUsername()).isPresent()){
            return null;
        }
        if (memberRepository.findByEmail(member.getEmail()).isPresent()){
            return null;
        }
        // Password encoding when register
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }


}
