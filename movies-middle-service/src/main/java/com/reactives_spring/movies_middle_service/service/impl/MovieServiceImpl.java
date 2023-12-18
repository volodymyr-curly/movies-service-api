package com.reactives_spring.movies_middle_service.service.impl;

import com.reactives_spring.movies_middle_service.client.MovieInfoRestClient;
import com.reactives_spring.movies_middle_service.client.ReviewRestClient;
import com.reactives_spring.movies_middle_service.domain.Movie;
import com.reactives_spring.movies_middle_service.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieInfoRestClient movieInfoClient;
    private final ReviewRestClient reviewClient;

    @Override
    public Mono<Movie> getMovieById(String movieId) {
        return movieInfoClient.getMovieInfoById(movieId)
                .flatMap(movieInfo -> {
                    var reviewsMono = reviewClient.getReviewsByMovieId(movieId)
                            .collectList();
                    return reviewsMono.map(reviews -> new Movie(movieInfo, reviews));
                });
    }
}
