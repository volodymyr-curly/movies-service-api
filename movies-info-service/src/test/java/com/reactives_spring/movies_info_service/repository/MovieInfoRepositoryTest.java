package com.reactives_spring.movies_info_service.repository;

import com.reactives_spring.movies_info_service.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryTest {

    @Autowired
    MovieInfoRepository repository;

    @BeforeEach
    void setUp() {
        var movieInfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        repository.saveAll(movieInfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void findAll() {
        //when
        var moviesInfoFlux = repository.findAll().log();
        //then
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        //when
        var movieInfoMono = repository.findById("abc").log();
        //then
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals("Dark Knight Rises", movieInfo.getName());
                    assertEquals(2012, movieInfo.getYear());
                })
                .verifyComplete();
    }

    @Test
    void findByYear() {
        //when
        var movieInfoMono = repository.findByYear(2012).log();
        //then
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals("Dark Knight Rises", movieInfo.getName());
                    assertEquals(2012, movieInfo.getYear());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {
        //given
        var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
        //when
        var movieInfoMono = repository.save(movieInfo).log();
        //then
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo1 -> {
                    assertNotNull(movieInfo1.getMovieInfoId());
                    assertEquals("Batman Begins1", movieInfo.getName());
                    assertEquals(2005, movieInfo.getYear());
                })
                .verifyComplete();
    }

    @Test
    void updateMovieInfo() {
        //given
        var movieInfo = repository.findById("abc").block();
        Objects.requireNonNull(movieInfo).setYear(2021);
        //when
        var movieInfoMono = repository.save(movieInfo).log();
        //then
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo1 -> assertEquals(2021, movieInfo.getYear()))
                .verifyComplete();
    }

    @Test
    void deleteMovieInfo() {
        //when
        repository.deleteById("abc").block();
        var moviesInfoFlux = repository.findAll().log();
        //then
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

}