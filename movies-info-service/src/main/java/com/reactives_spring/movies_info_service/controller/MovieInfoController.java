package com.reactives_spring.movies_info_service.controller;

import com.reactives_spring.movies_info_service.domain.MovieInfo;
import com.reactives_spring.movies_info_service.dto.MovieInfoRequest;
import com.reactives_spring.movies_info_service.service.impl.MovieInfoServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movie-infos")
@RequiredArgsConstructor
public class MovieInfoController {

    private final MovieInfoServiceImpl service;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfoRequest movieInfo) {
        return service.addMovieInfo(movieInfo).log();
    }

    @PutMapping("/{id}")
    public Mono<MovieInfo> updateMovieInfo(@PathVariable String id, @RequestBody @Valid MovieInfoRequest movieInfo) {
        return service.updateMovieInfo(id, movieInfo).log();
    }

    @GetMapping()
    public Flux<MovieInfo> getMovieInfosByParams(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) Integer year) {
        return service.getMovieInfosByParams(name, year).log();
    }

    @GetMapping("/{id}")
    public Mono<MovieInfo> getMovieInfoById(@PathVariable String id) {
        return service.getMovieInfoById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteMovieInfo(@PathVariable String id) {
        return service.deleteMovieInfo(id).log();
    }
}
