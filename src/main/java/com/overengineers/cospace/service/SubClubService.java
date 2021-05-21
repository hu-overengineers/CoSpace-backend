package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.*;
import com.overengineers.cospace.entity.Enrollment;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.Question;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.EventMapper;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SubClubService {

    private final SubClubRepository subClubRepository;

    private final MemberRepository memberRepository;

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    private final PostRepository postRepository;

    private final ReviewService reviewService;

    private final SecurityService securityService;
    private final EnrollmentService enrollmentService;

    private final QuestionRepository questionRepository;

    public SubClub getByName(String subClubName){
        if(!subClubRepository.findByName(subClubName).isPresent())
            return null;
        return subClubRepository.findByName(subClubName).get();
    }

    public List<SubClub> listAllSubClubs() {
        List<SubClub> subClubList = subClubRepository.findAll();
        return subClubList;
    }


    @Transactional
    public boolean isBanned(String subClubName) {
        return securityService.isMemberBannedFromSubClub(subClubName);
    }

    @Transactional
    public void updateSubClubRating(ReviewDTO reviewDTO, SubClub subClub) {
        int subClubReviewCount = reviewService.getSubClubReviews(subClub.getName()).size();
        int oldRatingSum = subClub.getRating() * (subClubReviewCount - 1);
        int newRatingSum = oldRatingSum + reviewDTO.getRating();

        subClub.setRating(newRatingSum / subClubReviewCount);

        subClubRepository.save(subClub);
    }

    @Transactional
    public ReviewDTO reviewToSubClub(ReviewDTO reviewDTO) {
        String subClubName = reviewDTO.getParentName();

        if (!securityService.isAuthorizedToSubClub(subClubName)) {
            return null; // Enrollment and Ban check
        }

        SubClub subClub = subClubRepository.findByName(subClubName).get();
        ReviewDTO savedReview = reviewService.createReviewFromDTO(reviewDTO, subClub);
        updateSubClubRating(reviewDTO, subClub);
        return savedReview;

    }

    public List<ReviewDTO> getReviewsByParentName(String subClubName) {
        if (securityService.isMemberBannedFromSubClub(subClubName))
            return null; // Ban Check
        return reviewService.getSubClubReviews(subClubName);
    }

    public List<SubClub> search(String query, Pageable pageable) {
        // Sort by rating, descending
        Pageable newPageable = UtilService.fixPageableSort(pageable, "rating", false);
        return subClubRepository.findByNameIgnoreCaseContaining(query, newPageable);
    }

    public List<Member> getMembers(String subClubName) {
        if (!securityService.isAuthorizedToSubClub(subClubName))
            return null;

        List<Member> members = enrollmentService.getSubClubMembers(subClubName);
        return members;
    }

    public List<EventDTO> getEvents(String subClubName) {
        Date convertedDatetime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

        return eventMapper.mapToDto(eventRepository.findByParent_NameAndDateAfter(subClubName,convertedDatetime ));
    }

    @Transactional
    public ResponseEntity<String> sendModRequest(String subClubName) {
        Optional<SubClub> optionalSubClub = subClubRepository.findByName(subClubName);
        if (optionalSubClub.isPresent()) {
            SubClub subClub = optionalSubClub.get();
            Member member = securityService.getAuthorizedMember();
            if(!securityService.isAuthorizedToSubClub(subClubName)) {
                return  ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED) // 401
                        .body("You are banned or not enrolled!");
            }
            if(securityService.isModBanned()) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED) // 401
                        .body("You were banned when you were a moderator!");
            }

            member.setModRequestedSubClubs(subClub);
            memberRepository.save(member);

            return ResponseEntity.ok("You are successfully applied.");
        }
        else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND) // 404
                    .body("SubClub not found!");
        }
    }

    public SubClubStatisticsDTO getStatistics(String subClubName, Date timeFrameStart, Date timeFrameEnd) {
        return new SubClubStatisticsDTO(enrollmentService.getSubClubMembers(subClubName).size(),
                postRepository.countAllByParentNameAndCreatedBetween(subClubName, timeFrameStart, timeFrameEnd));
    }

    public List<SubClub> listByParentName(String clubName) {
        return subClubRepository.findByParent_Name(clubName);
    }

    public List<Question> getQuestionnaire() {
        return enrollmentService.getQuestionnaire();
    }

    public List<Question> getQuestionnaireBySubClub(String subClubName) {
        return questionRepository.findByParent_Name(subClubName);
    }


    public ResponseEntity<String> enroll(List<QuestionDTO> answers ) {
        // TODO: Auth check vs return null, not member and ban check, isDismissed->null
        String authorizedUsername = securityService.getAuthorizedUsername();
        return enrollmentService.enroll(answers, authorizedUsername);
    }
}
