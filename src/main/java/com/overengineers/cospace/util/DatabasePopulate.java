package com.overengineers.cospace.util;

import com.overengineers.cospace.entity.*;
import com.overengineers.cospace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DatabasePopulate {

    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final SubClubRepository subClubRepository;
    private final PostRepository postRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public void populateDatabase() {

        authorityRepository.saveAll(Set.of(new Authority(null, null, "USER"), new Authority(null, null, "ADMIN")));

        List<String> adminList = new ArrayList<>(Arrays.asList("yusuf", "cagatay", "samil", "mert", "selim"));
        List<String> adminEmailList = new ArrayList<>(Arrays.asList("ketenyusuf", "cagatayyigit3", "m.samilatesoglu", "validatedev", "selim.seker00"));
        Authority adminAuthority = authorityRepository.findByAuthority("ADMIN");
        Authority userAuthority = authorityRepository.findByAuthority("USER");

        for (int i = 0; i < adminList.size(); i++) {
            String adminUsername = adminList.get(i);
            String adminPassword = passwordEncoder.encode("12345");
            String adminEmail = adminEmailList.get(i) + "@gmail.com";

            Member currentAdmin = new Member(adminUsername, adminPassword,adminEmail,null,null,null,null,null,Set.of(adminAuthority,userAuthority),null , null);
            memberRepository.save(currentAdmin);
        }


        int populationCount = 10;
        for (int i = 1; i <= populationCount; i++) {
            String memberUsername = "member" + i;
            String memberPassword = passwordEncoder.encode("123456");
            String memberEmail = memberUsername + "@gmail.com";
            Member member = new Member(memberUsername, memberPassword,memberEmail,null,null,null,null,null,Set.of(userAuthority),null , null);
            memberRepository.save(member);

            Club club = new Club("club" + i, "club" + i + " Details", null);
            clubRepository.save(club);

            SubClub sub = new SubClub("sub" + i, "sub" + i + " Details", 0, null, null, null, null, club, null, null, null,null);
            subClubRepository.save(sub);

            for(int j = 1; j <= 5; j++){
                Question question = new Question("q" + i + j,"a1","a2","a3", "a" + i + j, "a" + i + j, subClubRepository.findByName("sub" + i).get());
                Question savedQuestion = questionRepository.save(question);
            }

            for (int j = 0; j < 20; j++) {
                Post post = new Post("member" + i, "Title" + i, "This is a test content for sub" + i, i, null, subClubRepository.findByName("sub" + i).get());
                postRepository.save(post);
            }
        }

        for(int i = 1; i <= 5; i++){
            enrollmentRepository.save(new Enrollment(memberRepository.findByUsername(adminList.get(i - 1)), subClubRepository.findByName("sub" + i).get(), 100, true));
            enrollmentRepository.save(new Enrollment(memberRepository.findByUsername("member" + i), subClubRepository.findByName("sub" + i).get(), 100, true));
            SubClub subClub = subClubRepository.findByName("sub" + i).get();
            subClub.setModerator(memberRepository.findByUsername(adminList.get(i-1)));
            subClubRepository.save(subClub);
        }
    }
}
