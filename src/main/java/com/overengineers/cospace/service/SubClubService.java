package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.MemberDTO;
import com.overengineers.cospace.dto.ReviewDTO;
import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.Ban;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.repository.BanRepository;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SubClubService {

    private final SubClubRepository subClubRepository;
    private final SubClubMapper subClubMapper;

    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    private final ReviewService reviewService;

    private final SecurityService securityService;


    public List<SubClubDTO> listAllSubClubs(){
        List<SubClub> subClubList= subClubRepository.findAll();
        List<SubClubDTO> subClubDTOList = subClubMapper.mapToDto(subClubList);
        return subClubDTOList;
    }


    @Transactional
    public ResponseEntity<String> enroll(String subClubName){
        Optional<SubClub> optionalSubClub = subClubRepository.findByName(subClubName);
        if (optionalSubClub.isPresent()){
            SubClub subClub = optionalSubClub.get();
            Member member = securityService.getAuthorizedMember();

            member.getSubClubs().add(subClub);
            memberRepository.save(member);

            return ResponseEntity.ok("You are successfully enrolled.");
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404
                .body("SubClub not found!");
    }

    @Transactional
    public boolean isBanned(String subClubName){
        return securityService.isMemberBannedFromSubClub(subClubName);
    }

    @Transactional
    public void updateSubClubRating(ReviewDTO reviewDTO,  SubClub subClub){
        int subClubReviewCount = reviewService.getSubClubReviews(subClub.getName()).size();
        int oldRatingSum = subClub.getRating() * (subClubReviewCount - 1);
        int newRatingSum = oldRatingSum + reviewDTO.getRating();

        subClub.setRating(newRatingSum / subClubReviewCount);

        subClubRepository.save(subClub);
    }

    @Transactional
    public ReviewDTO reviewToSubClub(ReviewDTO reviewDTO) {
        String subClubName = reviewDTO.getParentName();

        if(!securityService.isAuthorizedToSubClub(subClubName)){
            return null; // Enrollment and Ban check
        }

        SubClub subClub = subClubRepository.findByName(subClubName).get();
        ReviewDTO savedReview = reviewService.createReviewFromDTO(reviewDTO, subClub);
        updateSubClubRating(reviewDTO, subClub);
        return savedReview;

    }

    public List<ReviewDTO> getReviewsByParentName(String subClubName) {
        if(securityService.isMemberBannedFromSubClub(subClubName))
            return null; // Ban Check
        return reviewService.getSubClubReviews(subClubName);
    }

    public List<SubClubDTO> search(String query, Pageable pageable){
        return subClubMapper.mapToDto(subClubRepository.findByNameIgnoreCaseContaining(query, pageable));
    }

    public List<MemberDTO> getMembers(String subClubName) {
        if(!securityService.isAuthorizedToSubClub(subClubName))
            return null;

        Set<Member> members = subClubRepository.findByName(subClubName).get().getMembers();
        return memberMapper.mapToDto(new ArrayList<>(members));
    }
}
