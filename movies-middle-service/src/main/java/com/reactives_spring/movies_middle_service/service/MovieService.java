package com.reactives_spring.movies_middle_service.service;

import com.reactives_spring.movies_middle_service.domain.Movie;
import reactor.core.publisher.Mono;

public interface MovieService {

    Mono<Movie> getMovieById(String movieId);
}
