package com.reactives_spring.movies_middle_service.controller;

import com.reactives_spring.movies_middle_service.domain.Movie;
import com.reactives_spring.movies_middle_service.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies")
@RequiredArgsConstructor
public class MoviesController {

    private final MovieService service;

    @GetMapping("/{id}")
    public Mono<Movie> getMovieById(@PathVariable("id") String movieId) {
        return service.getMovieById(movieId);
    }
}
