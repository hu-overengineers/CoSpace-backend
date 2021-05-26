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
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final int minimumInterestRate = 50;
    private final int subClubNumberForQuestionnaire = 4;
    private final int questionNumberPerSubClub = 3;

    public List<Member> getSubClubMembers(String subClubName){
        return memberRepository.findByEnrollments_SubClub_NameAndEnrollments_IsEnrolledTrue(subClubName);
    }

    public List<SubClub> getMemberSubClubs(String username){
        return subClubRepository.findByEnrollmentsMemberUsernameAndEnrollmentsIsEnrolledTrue(username);
    }

    public List<Question> getQuestionnaire() {
        List<Question> questions = new ArrayList<>();
        List<SubClub> allSubClubs = subClubRepository.findAll();

        if (allSubClubs.size() == 0)
            return null; // No SubClub

        // Remove the SubClubs that have not enough questions
        allSubClubs.removeIf(subClub -> subClub.getQuestions() == null || subClub.getQuestions().size() < questionNumberPerSubClub);

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
                enrollmentRepository.save(enrollment);
                enrolledSubClubNames.add(currentSubClub.getName());
            }
            else
            {
                Enrollment enrollment = new Enrollment(authorizedMember, currentSubClub, currentInterestRate, false);
                enrollmentRepository.save(enrollment);
            }

        }
        return ResponseEntity
                .status(HttpStatus.OK) // 200
                .body("Enrolled SubClubs: " + enrolledSubClubNames);
    }

    public ResponseEntity<Object> enrollSubClub(List<QuestionDTO> memberAnswers, String username){
        List<JSONObject> entities = new ArrayList<JSONObject>();
        String subClubName = memberAnswers.get(0).getParentName();
        int correctCount = 0;
        Optional<SubClub> optionalSubClub = subClubRepository.findByName(subClubName);
        if (!optionalSubClub.isPresent()){
            JSONObject entity = new JSONObject();
            entity.put("Message", "SubClub is not found");
            entities.add(entity);
            return new ResponseEntity<Object>(entities, HttpStatus.OK);
        }

        if(memberAnswers.size() < questionNumberPerSubClub)
        {
            JSONObject entity = new JSONObject();
            entity.put("Message", "At least" + questionNumberPerSubClub + "questions!");
            entities.add(entity);
            return new ResponseEntity<Object>(entities, HttpStatus.OK);
        }

        for (int i = 0; i < memberAnswers.size(); i++) {
            QuestionDTO currentQuestionDTO = memberAnswers.get(i);
            Optional<Question> optionalQuestion = questionRepository.findById(currentQuestionDTO.getId());
            if (!optionalQuestion.isPresent())
                continue;

            if (optionalQuestion.get().getGroundTruth().equals(currentQuestionDTO.getGroundTruth()))
                correctCount++;
        }

        float currentInterestRate = ((float)correctCount / memberAnswers.size()) * 100;

        SubClub currentSubClub = optionalSubClub.get();
        Member authorizedMember = memberRepository.findByUsername(username);

        if (currentInterestRate >= minimumInterestRate) {
            Enrollment enrollment = new Enrollment(authorizedMember, currentSubClub, currentInterestRate, true);
            enrollmentRepository.save(enrollment);

            JSONObject entity = new JSONObject();
            entity.put("Message", "Success");
            entities.add(entity);
            entity.put("SubClubName", subClubName);
            entities.add(entity);
            entity.put("Interest Rate", currentInterestRate);
            entities.add(entity);

            return new ResponseEntity<Object>(entities, HttpStatus.OK);
        }
        else
        {
            Enrollment enrollment = new Enrollment(authorizedMember, currentSubClub, currentInterestRate, false);
            enrollmentRepository.save(enrollment);
            JSONObject entity = new JSONObject();
            entity.put("Message", "Failed");
            entities.add(entity);
            entity.put("SubClubName", subClubName);
            entities.add(entity);
            entity.put("Interest Rate", currentInterestRate);
            entities.add(entity);

            return new ResponseEntity<Object>(entities, HttpStatus.OK);

        }
    }

    public Enrollment getEnrollmentByUsernameAndSubClubName(String username, String subClubName){
        return enrollmentRepository.findByMemberUsernameAndSubClubName(username, subClubName);
    }

    public List<Enrollment> getMyEnrollmentList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();
        return enrollmentRepository.findByMember_Username(username);
    }

}
