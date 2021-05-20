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

    public void populateDatabase(){

        authorityRepository.saveAll(Set.of(new Authority(null, null, "USER"), new Authority(null, null,"ADMIN")));

        List<String> adminList = new ArrayList<>(Arrays.asList("yusuf", "cagatay", "samil","mert","selim"));
        List<String> adminEmailList = new ArrayList<>(Arrays.asList("ketenyusuf", "cagatayyigit3", "m.samilatesoglu", "validatedev", "selim.seker00"));
        Authority adminAuthority = authorityRepository.findByAuthority("ADMIN");
        Authority userAuthority = authorityRepository.findByAuthority("USER");

        for(int i = 0; i < adminList.size(); i++){
            String adminUsername = adminList.get(i);
            String adminPassword = passwordEncoder.encode("12345");
            String adminEmail = adminEmailList.get(i) + "@gmail.com";

            Member currentAdmin = new Member(adminUsername, adminPassword,adminEmail,null,null,null,null,null,Set.of(adminAuthority,userAuthority),null,null, null);
            memberRepository.save(currentAdmin);
        }

        int populationCount = 3;
        for(int i = 1; i <= populationCount; i++){
            String memberUsername = "member" + i;
            String memberPassword = passwordEncoder.encode("123456");
            String memberEmail = memberUsername + "@gmail.com";
            Member member = new Member(memberUsername, memberPassword,memberEmail,null,null,null,null,null,Set.of(userAuthority),null,null, null);
            memberRepository.save(member);

            Club club = new Club("club" + i, "club" + i + " Details", null);
            clubRepository.save(club);

            SubClub sub = new SubClub("sub" + i, "sub" + i + " Details", 0, null, null, null, null, club, null, null, null,null);
            subClubRepository.save(sub);

            Post post = new Post("member" + i, "Title" + i, "This is a test content for sub" + i, i, null, subClubRepository.findByName("sub" + i).get());
            postRepository.save(post);

        }

    }
}
