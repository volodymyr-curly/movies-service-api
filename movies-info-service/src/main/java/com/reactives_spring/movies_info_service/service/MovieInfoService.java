package com.reactives_spring.movies_info_service.service;

import com.reactives_spring.movies_info_service.domain.MovieInfo;
import com.reactives_spring.movies_info_service.dto.MovieInfoRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieInfoService {

    Mono<MovieInfo> addMovieInfo(MovieInfoRequest movieInfo);

    Mono<MovieInfo> updateMovieInfo(String id, MovieInfoRequest movieInfo);

    Flux<MovieInfo> getMovieInfosByParams(String name, Integer year);

    Mono<MovieInfo> getMovieInfoById(String id);

    Mono<Void> deleteMovieInfo(String id);
}
