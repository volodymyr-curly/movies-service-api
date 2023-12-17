package com.reactives_spring.movies_review_service.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Review {

    @Id
    private String reviewId;

    @NotNull(message = "movieInfoId must not be null")
    private Long movieInfoId;

    private String comment;

    @Min(value = 0L, message = "rating must be positive or zero")
    private Double rating;
}
