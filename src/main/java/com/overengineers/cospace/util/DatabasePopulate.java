package com.overengineers.cospace.util;

import com.github.javafaker.Faker;
import com.overengineers.cospace.entity.*;
import com.overengineers.cospace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    Faker faker = new Faker();

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
        List<SubClub> generatedSubClubs = new ArrayList<>();
        for (int i = 1; i <= populationCount; i++) {
            String memberUsername = faker.name().username();
            String memberPassword = passwordEncoder.encode("123456");
            String memberEmail = memberUsername + "@gmail.com";
            Member member = new Member(memberUsername, memberPassword,memberEmail,null,null,null,null,null,Set.of(userAuthority),null , null);
            memberRepository.save(member);

            String clubName = String.join(" ", faker.lorem().words(faker.number().numberBetween(1, 3)));
            Club club = new Club(clubName, faker.lorem().sentence(), null);
            clubRepository.save(club);

            for (int k = 0; k < faker.number().numberBetween(1, 5); k++) {

                SubClub sub = new SubClub(String.join(" ", faker.lorem().words(faker.number().numberBetween(1, 3))),
                        faker.lorem().sentence(), 0, null, null, null, null, club, null, null, null,null);
                sub = subClubRepository.save(sub);

                for(int j = 1; j <= 5; j++){
                    String truth = faker.lorem().word();
                    Question question = new Question(faker.lorem().sentence(), truth, faker.lorem().word(), faker.lorem().word(), faker.lorem().word(),
                            truth, sub);
                    questionRepository.save(question);
                }

                for (int j = 0; j < faker.number().numberBetween(2, 40); j++) {
                    enrollmentRepository.save(new Enrollment(member, sub, 100, true));
                    Post post = new Post( memberUsername,
                            faker.lorem().sentence(),
                            String.join(" ", faker.lorem().sentences(faker.number().numberBetween(1, 10))), faker.number().numberBetween(-5, 100),
                            null, sub);
                    post.setCreated(faker.date().past(12 * 30, TimeUnit.DAYS));
                    postRepository.save(post);
                }

                generatedSubClubs.add(sub);
            }
        }

        for(int i = 1; i <= 5; i++){
            SubClub subClub = generatedSubClubs.get(i - 1);
            enrollmentRepository.save(new Enrollment(memberRepository.findByUsername(adminList.get(i - 1)), subClub, 100, true));
            subClub.setModerator(memberRepository.findByUsername(adminList.get(i-1)));
            subClubRepository.save(subClub);
        }
    }
}
