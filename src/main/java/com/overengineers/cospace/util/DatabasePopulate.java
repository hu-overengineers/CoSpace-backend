package com.overengineers.cospace.util;

import com.overengineers.cospace.entity.*;
import com.overengineers.cospace.repository.*;
import com.overengineers.cospace.service.CustomUserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DatabasePopulate {

    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final SubClubRepository subClubRepository;
    private final PostRepository postRepository;

    @Transactional
    public void populateDatabase(){
        List<Authority> savedAuthorities = authorityRepository.saveAll(Set.of(new Authority(null, null, "USER"), new Authority(null, null,"ADMIN")));
        List<String> adminList = new ArrayList<>(Arrays.asList("yusuf", "cagatay", "samil","mert","selim"));
        List<String> adminEmailList = new ArrayList<>(Arrays.asList("ketenyusuf", "cagatayyigit3", "m.samilatesoglu", "validatedev", "selim.seker00"));
        for(int i = 0; i < adminList.size(); i++){
            String currentUsername = adminList.get(i);
            String currentEmail = adminEmailList.get(i);
            Member currentAdmin = new Member(currentUsername, passwordEncoder.encode("12345"),currentEmail + "@gmail.com", null, Set.of(savedAuthorities.get(0), savedAuthorities.get(1)));
            memberRepository.save(currentAdmin);
        }

        Member memberTest = new Member("memberTest", passwordEncoder.encode("12345"), "memberTest@gmail.com", null, Set.of(savedAuthorities.get(0)));
        memberRepository.save(memberTest);

        Club clubTest = new Club("ClubTest", "ClubTest Details", 0,null, null, null);
        clubRepository.save(clubTest);

        SubClub subTest = new SubClub("SubTest", "SubTest Details", "ClubTest", 0, null, clubRepository.findByClubName("ClubTest").get());
        subClubRepository.save(subTest);

        Post postTest = new Post("memberTest", "TitleTest", "This is a test content", "SubTest", 0, null, subClubRepository.findBySubClubName("SubTest").get());
        postRepository.save(postTest);

    }
}
