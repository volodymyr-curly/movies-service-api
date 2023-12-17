package com.reactives_spring.movies_review_service.validator;

import com.reactives_spring.movies_review_service.domain.Review;
import com.reactives_spring.movies_review_service.exception.ReviewDataException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.joining;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewValidator {

    private final Validator validator;

    public void validate(Review review) {
        var constraintViolations = validator.validate(review);
        log.info("constraintViolations : {}", constraintViolations);
        if (!constraintViolations.isEmpty()) {
            var errorMessage = constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(joining(","));
            throw new ReviewDataException(errorMessage);
        }
    }
}
