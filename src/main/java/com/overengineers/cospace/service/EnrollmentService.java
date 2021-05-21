package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.QuestionDTO;
import com.overengineers.cospace.entity.Enrollment;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.Question;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.repository.EnrollmentRepository;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.repository.QuestionRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final MemberRepository memberRepository;
    private final SubClubRepository subClubRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final QuestionRepository questionRepository;

    private int minimumInterestRate = 50;
    private int subClubNumberForQuestionnaire = 6;
    private int questionNumberPerSubClub = 3;

    public List<Member> getSubClubMembers(String subClubName){
        return memberRepository.findByEnrollments_SubClub_Name(subClubName);
    }

    public List<SubClub> getMemberSubClubs(String username){
        return subClubRepository.findByEnrollments_Member_Username(username);
    }

    /*
    @Transactional
    public ResponseEntity<String> enroll(String subClubName, List<QuestionDTO> memberAnswers) {

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
*/
    public List<Question> getQuestionnaire() {
        List<Question> questions = new ArrayList<>();
        List<SubClub> allSubClubs = subClubRepository.findAll();
        if (allSubClubs == null || allSubClubs.size() == 0)
            return null; // No SubClub

        for(SubClub subClub: allSubClubs){
            if(subClub.getQuestions() == null ||subClub.getQuestions().size() < questionNumberPerSubClub)
                allSubClubs.remove(subClub); // Remove the SubClubs that have not enough questions
        }

        int subClubCount = allSubClubs.size();
        Set<Integer> subClubIndices = UtilService.pickRandom(subClubNumberForQuestionnaire, subClubCount);

        for (int subClubIdx : subClubIndices){
            SubClub currentSubClub = allSubClubs.get(subClubIdx);
            List<Question> questionnaireForCurrentSubClub = questionRepository.findByParent_Name(currentSubClub.getName());

            Set<Integer> questionIndices = UtilService.pickRandom(questionNumberPerSubClub, questionnaireForCurrentSubClub.size());

            for(int questionIdx : questionIndices){
                Question currentQuestion = questionnaireForCurrentSubClub.get(questionIdx);
                questions.add(currentQuestion);
            }
        }
        return questions;
    }

}
