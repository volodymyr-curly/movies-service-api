package com.reactives_spring.movies_info_service.service.impl;

import com.reactives_spring.movies_info_service.domain.MovieInfo;
import com.reactives_spring.movies_info_service.dto.MovieInfoRequest;
import com.reactives_spring.movies_info_service.exception.InfoNotFountException;
import com.reactives_spring.movies_info_service.mapper.MovieInfoMapper;
import com.reactives_spring.movies_info_service.repository.MovieInfoRepository;
import com.reactives_spring.movies_info_service.service.MovieInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MovieInfoServiceImpl implements MovieInfoService {

    private final MovieInfoRepository repository;
    private final MovieInfoMapper mapper;

    public Mono<MovieInfo> addMovieInfo(MovieInfoRequest movieInfo) {
        return repository.save(mapper.toMovieInfoWithId(movieInfo)).log();
    }

    @Override
    public Mono<MovieInfo> updateMovieInfo(String id, MovieInfoRequest movieInfo) {
        return repository.findById(id)
                .flatMap(info -> repository.save(mapper.toMovieInfo(movieInfo)));
    }

    @Override
    public Flux<MovieInfo> getMovieInfosByParams(String name, Integer year) {
        if (Objects.nonNull(name) && Objects.nonNull(year)) {
            return repository.findByNameAndYear(name, year);
        } else if (Objects.nonNull(name)) {
            return repository.findByName(name);
        } else if (Objects.nonNull(year)) {
            return repository.findByYear(year);
        } else {
            return repository.findAll();
        }
    }

    @Override
    public Mono<MovieInfo> getMovieInfoById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new InfoNotFountException("Movie info not found")));
    }

    @Override
    public Mono<Void> deleteMovieInfo(String id) {
        return repository.deleteById(id);
    }
}
