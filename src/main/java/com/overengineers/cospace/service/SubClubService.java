package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.ReviewDTO;
import com.overengineers.cospace.dto.SubClubDTO;
import com.overengineers.cospace.entity.Review;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.ReviewMapper;
import com.overengineers.cospace.mapper.SubClubMapper;
import com.overengineers.cospace.repository.ReviewRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubClubService {

    private final SubClubRepository subClubRepository;
    private final SubClubMapper subClubMapper;

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public List<SubClubDTO> listAllSubClubs(){
        List<SubClub> subClubList= subClubRepository.findAll();
        List<SubClubDTO> subClubDTOList = subClubMapper.mapToDto(subClubList);
        return subClubDTOList;
    }

    public SubClub saveNewSubClub(SubClub subClub){ return subClubRepository.save(subClub); }

    public ReviewDTO review(ReviewDTO reviewDTO) {
        Optional<SubClub> optionalSubClub = subClubRepository.findByName(reviewDTO.getParentName());
        if(!optionalSubClub.isPresent()){
            return null;
        }
        else{
            SubClub currentSubClub = optionalSubClub.get();
            Review newReview = reviewMapper.mapToEntity(reviewDTO);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            newReview.setAuthor(username);
            newReview.setParent(currentSubClub);
            Review savedReview = reviewRepository.save(newReview);

            List<Review> subClubReviews = reviewRepository.findByParentName(currentSubClub.getName());
            currentSubClub = subClubRepository.findByName(reviewDTO.getParentName()).get();
            int oldRatingSum = currentSubClub.getRating() * (subClubReviews.size() - 1);
            int newRatingSum = oldRatingSum + reviewDTO.getRating();
            currentSubClub.setRating(newRatingSum / subClubReviews.size());
            subClubRepository.save(currentSubClub);
            return reviewMapper.mapToDto(savedReview);
        }
    }

    public List<ReviewDTO> getReviewsByParentName(String subClubName) {
        return reviewMapper.mapToDto(reviewRepository.findByParentName(subClubName));
    }
}
