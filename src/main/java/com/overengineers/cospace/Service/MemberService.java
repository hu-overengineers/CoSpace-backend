package com.overengineers.cospace.Service;

import com.overengineers.cospace.Entity.Member;
import com.overengineers.cospace.Mapper.MemberMapper;
import com.overengineers.cospace.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
