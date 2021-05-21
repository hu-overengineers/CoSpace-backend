package com.overengineers.cospace.service;

import com.overengineers.cospace.entity.Enrollment;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.repository.EnrollmentRepository;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final MemberRepository memberRepository;
    private final SubClubRepository subClubRepository;
    private final EnrollmentRepository enrollmentRepository;

    private final SecurityService securityService;

    private int minimumInterestRate = 50;

    public List<Member> getSubClubMembers(String subClubName){
        return memberRepository.findByEnrollments_SubClub_Name(subClubName);
    }

    public List<SubClub> getMemberSubClubs(String username){
        return subClubRepository.findByEnrollments_Member_Username(username);
    }
    
    @Transactional
    public ResponseEntity<String> enroll(String subClubName, int interestRate) {

        Optional<SubClub> subClub = subClubRepository.findByName(subClubName);
        if(!subClub.isPresent()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404
                    .body("SubClub not found!");
        }
        else {
            Member member = securityService.getAuthorizedMember();
            Enrollment enrollment = new Enrollment(member, subClub.get(), interestRate);
            Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

            // return savedEnrollment with GenericResponse new implementation
            return ResponseEntity.ok("You are successfully enrolled.");
        }

    }

}
