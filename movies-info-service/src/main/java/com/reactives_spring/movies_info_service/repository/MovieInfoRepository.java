package com.reactives_spring.movies_info_service.repository;

import com.reactives_spring.movies_info_service.domain.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

    Flux<MovieInfo> findByYear(Integer year);

    Flux<MovieInfo> findByName(String name);

    Flux<MovieInfo> findByNameAndYear(String name, Integer year);
}
