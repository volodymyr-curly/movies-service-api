package com.reactives_spring.movies_info_service.controller;

import com.reactives_spring.movies_info_service.domain.MovieInfo;
import com.reactives_spring.movies_info_service.dto.MovieInfoRequest;
import com.reactives_spring.movies_info_service.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MovieInfoControllerTest {

    private static final String MOVIE_INFOS_URL = "/v1/movie-infos";
    private static final String MOVIE_INFO_URL = MOVIE_INFOS_URL + "/{id}";

    @Autowired
    MovieInfoRepository repository;

    @Autowired
    WebTestClient client;

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
    void whenAddMovieInfo_thenSuccess() {
        var movieInfo = new MovieInfoRequest(null, "Batman Begins",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        client
                .post()
                .uri(MOVIE_INFOS_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert savedMovieInfo != null;
                    assert savedMovieInfo.getMovieInfoId() != null;
                });
    }

    @Test
    void whenAddMovieInfo_thenValidationError() {
        var movieInfo = new MovieInfoRequest();
        String expectedMessage = "name must not be blank,release_date must not be null,year must not be null";
        client
                .post()
                .uri(MOVIE_INFOS_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    var responseBody = stringEntityExchangeResult.getResponseBody();
                    assertEquals(expectedMessage, responseBody);
                });
    }

    @Test
    void whenUpdateMovieInfoById_thenSuccess() {
        String id = "abc";
        var updatedMovieInfo = new MovieInfoRequest(null, "Dark Knight Rises1",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        client
                .put()
                .uri(MOVIE_INFO_URL, id)
                .bodyValue(updatedMovieInfo)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises1");
    }

    @Test
    void whenGetMovieInfosByParams_thenSuccess() {
        //get movies infos without params
        client
                .get()
                .uri(MOVIE_INFOS_URL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
        //get movies infos by name and year
        client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(MOVIE_INFOS_URL)
                        .queryParam("name", "Batman Begins")
                        .queryParam("year", 2005)
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(1);
        //get movies infos by name
        client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(MOVIE_INFOS_URL)
                        .queryParam("name", "Batman Begins")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(1);
    }

    @Test
    void whenGetMovieInfoById_thenSuccess() {
        String id = "abc";

        client
                .get()
                .uri(MOVIE_INFO_URL, id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Dark Knight Rises");
    }

    @Test
    void whenGetMovieInfoById_thenNotFound() {
        String id = "123";

        client
                .get()
                .uri(MOVIE_INFO_URL, id)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$").isEqualTo("Movie info not found");
    }

    @Test
    void whenDeleteMovieInfo_thenSuccess() {
        String id = "abc";
        //delete movie info
        client
                .delete()
                .uri(MOVIE_INFO_URL, id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Void.class);
        //check movie info's absents
        client
                .get()
                .uri(MOVIE_INFOS_URL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(2);
    }
}