package com.reactives_spring.movies_middle_service.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private String reviewId;

    @NotNull(message = "movieInfoId must not be null")
    private Long movieInfoId;

    private String comment;

    @Min(value = 0L, message = "rating must be positive or zero")
    private Double rating;
}
