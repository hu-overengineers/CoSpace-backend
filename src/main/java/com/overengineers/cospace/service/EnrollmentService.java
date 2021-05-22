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
        return memberRepository.findByEnrollments_SubClub_NameAndEnrollments_IsEnrolledTrue(subClubName);
    }

    public List<SubClub> getMemberSubClubs(String username){
        return subClubRepository.findByEnrollments_Member_UsernameAndEnrollments_IsEnrolledTrue(username);
    }

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

    @Transactional
    public ResponseEntity<String> enroll(List<QuestionDTO> memberAnswers, String username) {
        List<String> enrolledSubClubNames = new ArrayList<>();
        for (int i = 0; i < subClubNumberForQuestionnaire; i++) {
            int correctCount = 0;
            int subClubIdx = i * questionNumberPerSubClub + 1;
            String subClubName = memberAnswers.get(subClubIdx).getParentName();
            Optional<SubClub> optionalSubClub = subClubRepository.findByName(subClubName);
            if (!optionalSubClub.isPresent())
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND) // 404
                        .body("SubClub not found!");

            for (int j = 0; j < questionNumberPerSubClub; j++) {
                QuestionDTO currentQuestionDTO = memberAnswers.get(i * questionNumberPerSubClub + j);
                Optional<Question> optionalQuestion = questionRepository.findById(currentQuestionDTO.getId());
                if (!optionalQuestion.isPresent())
                    continue;

                if (optionalQuestion.get().getGroundTruth().equals(currentQuestionDTO.getGroundTruth()))
                    correctCount++;
            }

            float currentInterestRate = ((float)correctCount / questionNumberPerSubClub) * 100;

            SubClub currentSubClub = optionalSubClub.get();
            Member authorizedMember = memberRepository.findByUsername(username);

            if (currentInterestRate >= minimumInterestRate) {
                Enrollment enrollment = new Enrollment(authorizedMember, currentSubClub, currentInterestRate, true);
                Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
                enrolledSubClubNames.add(currentSubClub.getName());
            }
            else
            {
                Enrollment enrollment = new Enrollment(authorizedMember, currentSubClub, currentInterestRate, false);
                Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
            }

        }
        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body("Enrolled SubClubs: " + enrolledSubClubNames.toString());
    }

    public Enrollment getEnrollmentByUsernameAndSubClubName(String username, String subClubName){
        return enrollmentRepository.findByMemberUsernameAndSubClubName(username, subClubName);
    }

}
