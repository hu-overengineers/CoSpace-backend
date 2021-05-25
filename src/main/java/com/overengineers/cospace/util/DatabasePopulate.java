package com.overengineers.cospace.util;

import com.github.javafaker.Faker;
import com.overengineers.cospace.entity.*;
import com.overengineers.cospace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

            Member currentAdmin = new Member(adminUsername, adminPassword, adminEmail, null, null, null, null, null, Set.of(adminAuthority, userAuthority), null, null);
            memberRepository.save(currentAdmin);
        }

        int clubCount = 10;
        int maxSubClubCountPerClub = 5;
        int memberCount = 100;

        List<Club> generatedClubs = new ArrayList<>();
        for (int c = 1; c <= clubCount; c++) {
            String clubName = String.join(" ", faker.lorem().words(faker.number().numberBetween(1, 3)));
            if (clubRepository.findByName(clubName).isPresent()) continue;
            Club club = new Club(clubName, faker.lorem().sentence(), null);
            generatedClubs.add(clubRepository.save(club));
        }

        List<SubClub> generatedSubClubs = new ArrayList<>();
        for (Club club : generatedClubs) {
            for (int k = 0; k < faker.number().numberBetween(1, maxSubClubCountPerClub); k++) {
                String subClubName =
                        String.join(" ", faker.lorem().words(faker.number().numberBetween(1, 3)));
                SubClub sub = new SubClub(
                        subClubName,
                        faker.lorem().sentence(),
                        0, null, null, null, null,
                        club,
                        null, null, null, null);

                if (clubRepository.findByName(subClubName).isPresent()) continue;
                if (subClubRepository.findByName(subClubName).isPresent()) continue;

                generatedSubClubs.add(subClubRepository.save(sub));

                for (int j = 1; j <= faker.number().numberBetween(5, 10); j++) {
                    String truth = faker.lorem().word();
                    Question question = new Question(
                            faker.lorem().sentence(),
                            truth,
                            faker.lorem().word(),
                            faker.lorem().word(),
                            faker.lorem().word(),
                            truth,
                            sub);
                    questionRepository.save(question);
                }
            }
        }
        List<Member> generatedMembers = new ArrayList<>();
        for (int m = 0; m < memberCount; m++) {
            String memberUsername = faker.name().username();
            String memberPassword = passwordEncoder.encode("123456");
            String memberEmail = memberUsername + "@gmail.com";
            Member member = new Member(memberUsername, memberPassword, memberEmail, null, null, null, null, null, Set.of(userAuthority), null, null);
            generatedMembers.add(memberRepository.save(member));
        }

        for (Member member : generatedMembers) {
            int enrolledCount = faker.number().numberBetween(0, clubCount * maxSubClubCountPerClub);
            for (int i = 0; i < enrolledCount; i++) {
                SubClub randomSubClub = generatedSubClubs.get(faker.number().numberBetween(0, generatedSubClubs.size() - 1));
                List<Enrollment> enrollments = enrollmentRepository.findBySubClub_Name(member.getUsername());
                if (enrollments.stream().noneMatch(enrollment -> enrollment.getMember().getUsername().equals(member.getUsername()))) {
                    enrollmentRepository.save(new Enrollment(member, randomSubClub, faker.number().numberBetween(50, 100), true));
                }
            }
        }

        for (Member member : generatedMembers) {
            int postCount = faker.number().numberBetween(0, 20);
            member = memberRepository.findByUsername(member.getUsername());

            for (int i = 0; i < postCount; i++) {
                List<SubClub> subClubs = subClubRepository.findByEnrollmentsMemberUsernameAndEnrollmentsIsEnrolledTrue(member.getUsername());
                if (subClubs.size() == 0) continue;
                SubClub randomSubClub = subClubs.get(faker.number().numberBetween(0, subClubs.size() - 1));
                Post post = new Post(member.getUsername(),
                        faker.lorem().sentence(),
                        String.join(" ", faker.lorem().sentences(faker.number().numberBetween(1, 10)))
                                + String.format("<p><img src=\"%s\" alt=\"undefined\" style=\"height: auto;width: 480\"/></p>",
                                faker.internet().image()), faker.number().numberBetween(-5, 100),
                        null, randomSubClub);
                post.setCreated(faker.date().past(12 * 30, TimeUnit.DAYS));
                postRepository.save(post);
            }
        }

        for (int i = 1; i <= 5; i++) {
            SubClub subClub = generatedSubClubs.get(i - 1);
            enrollmentRepository.save(new Enrollment(memberRepository.findByUsername(adminList.get(i - 1)), subClub, 100, true));
            subClub.setModerator(memberRepository.findByUsername(adminList.get(i - 1)));
            subClubRepository.save(subClub);
        }
    }
}
