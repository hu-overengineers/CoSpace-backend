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

            Member currentAdmin = new Member(adminUsername, adminPassword,adminEmail,null,null,null,null,null,Set.of(adminAuthority,userAuthority),null,null, null);
            memberRepository.save(currentAdmin);
        }

        int populationCount = 5;
        for (int i = 1; i <= populationCount; i++) {
            String memberUsername = "member" + i;
            String memberPassword = passwordEncoder.encode("123456");
            String memberEmail = memberUsername + "@gmail.com";
            Member member = new Member(memberUsername, memberPassword,memberEmail,null,null,null,null,null,Set.of(userAuthority),null,null, null);
            memberRepository.save(member);

            Club club = new Club("club" + i, "club" + i + " Details", null);
            clubRepository.save(club);

            // TODO: Questionnaire should be added to SubClub
            
            SubClub sub = new SubClub("sub" + i, "sub" + i + " Details", 0, null, null, null, null, club, memberRepository.findByUsername(adminList.get(i-1)), null, null,null);
            subClubRepository.save(sub);

            Enrollment enrollment = new Enrollment(member, sub, 100, true);
            enrollmentRepository.save(enrollment);

            for (int j = 0; j < 100; j++) {
                Post post = new Post("member" + i, "Title" + i, "This is a test content for sub" + i, i, null, subClubRepository.findByName("sub" + i).get());
                postRepository.save(post);
            }
        }
        enrollmentRepository.save(new Enrollment(memberRepository.findByUsername("yusuf"), subClubRepository.findByName("sub1").get(), 100, true));
        enrollmentRepository.save(new Enrollment(memberRepository.findByUsername("cagatay"), subClubRepository.findByName("sub2").get(), 100, true));
        enrollmentRepository.save(new Enrollment(memberRepository.findByUsername("samil"), subClubRepository.findByName("sub3").get(), 100, true));
        enrollmentRepository.save(new Enrollment(memberRepository.findByUsername("mert"), subClubRepository.findByName("sub4").get(), 100, true));
        enrollmentRepository.save(new Enrollment(memberRepository.findByUsername("selim"), subClubRepository.findByName("sub5").get(), 100, true));

    }
}
