package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.ReviewDTO;
import com.overengineers.cospace.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "review", target = "parentName", qualifiedByName = "parentName")
    ReviewDTO mapToDto(Review review);

    @Named("parentName")
    default String parentToParentName(Review review){
        return review.getParent().getName();
    }

    Review mapToEntity(ReviewDTO reviewDTO);

    List<ReviewDTO> mapToDto(List<Review> reviewList);

    List<Review> mapToEntity(List<ReviewDTO> reviewDTOList);

}
