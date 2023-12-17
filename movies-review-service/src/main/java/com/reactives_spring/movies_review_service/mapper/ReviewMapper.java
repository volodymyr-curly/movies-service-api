package com.reactives_spring.movies_review_service.mapper;

import com.reactives_spring.movies_review_service.domain.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReviewMapper {

    @Mapping(target = "movieInfoId", ignore = true)
    Review toReview(Review movieInfo);
}
