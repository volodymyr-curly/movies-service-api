package com.reactives_spring.movies_middle_service.controller;

import com.reactives_spring.movies_middle_service.domain.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8083)
@TestPropertySource(
        properties = {
                "restClient.moviesInfoUrl=http://localhost:8083/v1/movie-infos",
                "restClient.reviewsUrl=http://localhost:8083/v1/reviews"
        }
)
class MoviesControllerTest {

    @Autowired
    WebTestClient client;

    @Test
    void whenGetMovieById_thenSuccess() {
        String movieId = "abc";
        stubFor(get(urlEqualTo("/v1/movie-infos/" + movieId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("movie-info.json")));

        stubFor(get(urlPathEqualTo("/v1/reviews"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("reviews.json")));

        client
                .get()
                .uri("/v1/movies/{id}", movieId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Movie.class)
                .consumeWith(movieExchangeResult -> {
                    var movie = movieExchangeResult.getResponseBody();
                    assert movie != null;
                    assertEquals("Batman Begins", movie.getMovieInfo().getName());
                    assertEquals(2, movie.getReviews().size());
                });
    }

    @Test
    void whenGetMovieById_thenMovieInfoNotFound() {
        String movieId = "abc";
        stubFor(get(urlEqualTo("/v1/movie-infos/" + movieId))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())));

        client
                .get()
                .uri("/v1/movies/{id}", movieId)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(String.class)
                .isEqualTo("There is no movie info available for the passed id : abc");
    }

    @Test
    void whenGetMovieById_thenServerError() {
        String movieId = "abc";
        stubFor(get(urlEqualTo("/v1/movie-infos/" + movieId))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withBody("MovieInfoService unavailable")));

        client
                .get()
                .uri("/v1/movies/{id}", movieId)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Server exception in MoviesInfoService : MovieInfoService unavailable");
    }
}