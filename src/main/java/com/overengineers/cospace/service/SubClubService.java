package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.*;
import com.overengineers.cospace.entity.*;
import com.overengineers.cospace.mapper.EventMapper;
import com.overengineers.cospace.repository.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final SearchService searchService;
    private final MailService mailService;

    private final QuestionRepository questionRepository;

    public SubClub getByName(String subClubName) {
        return subClubRepository.findByName(subClubName).orElse(null);
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

        // TODO: Enrolment check needed
        //if (!securityService.isAuthorizedToSubClub(subClubName)) {
        //    return null; // Enrollment and Ban check
        //}

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
        return searchService.searchSubClubs(query, newPageable).toList();
    }

    public List<Member> getMembers(String subClubName) {
        if (!securityService.isAuthorizedToSubClub(subClubName))
            return null;

        List<Member> members = enrollmentService.getSubClubMembers(subClubName);
        return members;
    }

    public List<EventDTO> getEvents(String subClubName) {
        return eventMapper.mapToDto(eventRepository.findByParent_Name(subClubName));
    }

    @Transactional
    public Event attendEvent(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (!securityService.isAuthorizedToSubClub(event.getParent().getName()))
                return null;
            Member member = securityService.getAuthorizedMember();
            member.getAttendedEvents().add(event);
            memberRepository.save(member);
            event.getParticipants().add(member);
            return eventRepository.save(event);
        }
        return null;
    }

    @Transactional
    public ResponseEntity<String> sendModRequest(String subClubName) {
        Optional<SubClub> optionalSubClub = subClubRepository.findByName(subClubName);
        if (optionalSubClub.isPresent()) {
            SubClub subClub = optionalSubClub.get();
            Member member = securityService.getAuthorizedMember();

            if(member.getModeratorSubClub() != null)
                return  ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED) // 401
                        .body("You are a moderator of another sub-club!");

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

    public ResponseEntity<Object> enrollSubClub(List<QuestionDTO> answers ) {
        // TODO: Auth check vs return null, not member and ban check, isDismissed->null
        String authorizedUsername = securityService.getAuthorizedUsername();
        String subClubName = answers.get(0).getParentName();
        Enrollment existedEnrollment = enrollmentService.getEnrollmentByUsernameAndSubClubName(authorizedUsername,subClubName);

        List<JSONObject> entities = new ArrayList<JSONObject>();

        if(existedEnrollment != null) {
            JSONObject entity = new JSONObject();
            entity.put("Message", "Already enrolled");
            entities.add(entity);
        }
        else{
            return enrollmentService.enrollSubClub(answers, authorizedUsername);
        }
        return new ResponseEntity<Object>(entities, HttpStatus.OK);

    }

    public List<SubClub> getCommonSubClubs(String sourceUsername, String targetUsername) {
        List<SubClub> authorSubClubs = enrollmentService.getMemberSubClubs(securityService.getAuthorizedUsername());
        if(authorSubClubs == null)
            return null;

        Member targetMember = memberRepository.findByUsername(targetUsername);
        if(targetMember == null)
            return null;

        List<SubClub> targetSubClubs = enrollmentService.getMemberSubClubs(targetUsername);
        if (targetSubClubs == null)
            return null;

        Set<SubClub> intersection = new HashSet<>(authorSubClubs);
        intersection.retainAll(targetSubClubs);
        return new ArrayList<>(intersection);
    }

    private Map<String, String> getMemberMailsMap(String subClubName){
        List<Member> members = enrollmentService.getSubClubMembers(subClubName);
        Map<String, String> map = new HashMap<>();
        for(Member member : members){
            map.put(member.getUsername(), member.getEmail());
        }
        return map;
    }

    @Scheduled(cron="0 0 13 * * ?")
    public void scheduleFixedDelayTask() { // Every day 13:00
        int maxInactiveDays = 90;
        int mailSentDayBefore = 7; // Mail will at day 83
        List<SubClub> subClubList = subClubRepository.findAll();

        for(SubClub sub : subClubList){
            if(UtilService.differenceDays(UtilService.now(), sub.getLastModified()) == maxInactiveDays - mailSentDayBefore){
                mailService.sendSubClubDeleteMail(getMemberMailsMap(sub.getName()), sub.getName());
            }

            if(UtilService.isSubClubInactive(sub, maxInactiveDays)){
                subClubRepository.deleteById(sub.getId());
            }
        }

    }
}
