package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.ReviewDTO;
import com.overengineers.cospace.entity.Review;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.ReviewMapper;
import com.overengineers.cospace.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;

    private final SecurityService securityService;

    @Transactional
    public ReviewDTO createReviewFromDTO(ReviewDTO reviewDTO, SubClub subClub){
        Review newReview = reviewMapper.mapToEntity(reviewDTO);
        newReview.setAuthor(securityService.getAuthorizedUsername());
        newReview.setParent(subClub);
        Review savedReview = reviewRepository.save(newReview);
        return reviewMapper.mapToDto(savedReview);
    }

    public List<ReviewDTO> getSubClubReviews(String subClubName){
        return reviewMapper.mapToDto(reviewRepository.findByParentName(subClubName));
    }


}
