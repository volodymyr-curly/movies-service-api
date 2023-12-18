package com.reactives_spring.movies_middle_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    private MovieInfo movieInfo;
    private List<Review> reviews;
}
