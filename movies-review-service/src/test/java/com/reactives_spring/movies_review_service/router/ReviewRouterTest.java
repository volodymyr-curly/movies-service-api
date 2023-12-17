package com.reactives_spring.movies_review_service.router;

import com.reactives_spring.movies_review_service.domain.Review;
import com.reactives_spring.movies_review_service.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class ReviewRouterTest {

    private static final String REVIEWS_URL = "/v1/reviews";
    private static final String REVIEW_URL = REVIEWS_URL + "/{id}";

    @Autowired
    ReviewRepository repository;

    @Autowired
    WebTestClient client;

    @BeforeEach
    void setUp() {
        var reviewsList = List.of(
                new Review(null, 1L, "Awesome Movie", 9.0),
                new Review(null, 1L, "Awesome Movie1", 9.0),
                new Review("abc", 2L, "Excellent Movie", 8.0));
        repository.saveAll(reviewsList)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    void whenAddReview_thenSuccess() {
        var movieInfo = new Review(null, 1L, "Awesome Movie", 9.0);

        client
                .post()
                .uri(REVIEWS_URL)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Review.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var savedReview = movieInfoEntityExchangeResult.getResponseBody();
                    assert savedReview != null;
                    assert savedReview.getMovieInfoId() != null;
                });
    }

    @Test
    void whenAddReview_thenValidationError() {
        var review = new Review(null, null, "Awesome Movie", -9.0);
        String expectedMessage = "movieInfoId must not be null,rating must be positive or zero";
        client
                .post()
                .uri(REVIEWS_URL)
                .bodyValue(review)
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
    void whenUpdateReview_thenSuccess() {
        String id = "abc";
        var updatedReview = new Review(null, 2L, "Super Movie", 8.0);

        client
                .put()
                .uri(REVIEW_URL, id)
                .bodyValue(updatedReview)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.comment").isEqualTo("Super Movie");
    }

    @Test
    void whenUpdateReview_thenValidationError() {
        String id = "abc";
        var updatedReview = new Review(null, null, "Super Movie", -8.0);
        String expectedMessage = "movieInfoId must not be null,rating must be positive or zero";

        client
                .put()
                .uri(REVIEW_URL, id)
                .bodyValue(updatedReview)
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
    void whenUpdateReview_thenNotFoundError() {
        String id = "123";
        var updatedReview = new Review(null, 1L, "Super Movie", 8.0);

        client
                .put()
                .uri(REVIEW_URL, id)
                .bodyValue(updatedReview)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .jsonPath("$").isEqualTo("review not found");
    }

    @Test
    void whenDeleteReview_thenSuccess() {
        String id = "abc";
        //delete movie info
        client
                .delete()
                .uri(REVIEW_URL, id)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class);
        //check movie info's absents
        client
                .get()
                .uri(REVIEWS_URL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Review.class)
                .hasSize(2);
    }

    @Test
    void whenGetMovieInfosByParams_thenSuccess() {
        //get reviews without params
        client
                .get()
                .uri(REVIEWS_URL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Review.class)
                .hasSize(3);
        //get reviews by movie id
        client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(REVIEWS_URL)
                        .queryParam("movieInfoId", "1")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Review.class)
                .hasSize(2);
    }
}