package com.overengineers.cospace.util;

import com.overengineers.cospace.entity.*;
import com.overengineers.cospace.repository.*;
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

        int populationCount = 3;
        for(int i = 1; i <= populationCount; i++){

            Member member = new Member("member" + i, passwordEncoder.encode("123456"), "member" + i + "@gmail.com", null, Set.of(savedAuthorities.get(0)));
            memberRepository.save(member);

            Club club = new Club("club" + i, "club" + i + " Details",null, null, null);
            clubRepository.save(club);

            SubClub sub = new SubClub("sub" + i, "sub" + i + " Details", 0, null, null, clubRepository.findByName("club" + i).get());
            subClubRepository.save(sub);
            
            Post post = new Post("member" + i, "Title" + i, "This is a test content for sub" + i, i, null, subClubRepository.findByName("sub" + i).get());
            postRepository.save(post);

        }

    }
}
