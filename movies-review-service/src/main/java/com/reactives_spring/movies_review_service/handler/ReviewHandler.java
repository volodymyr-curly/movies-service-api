package com.reactives_spring.movies_review_service.handler;

import com.reactives_spring.movies_review_service.domain.Review;
import com.reactives_spring.movies_review_service.exception.ReviewNotFoundException;
import com.reactives_spring.movies_review_service.mapper.ReviewMapper;
import com.reactives_spring.movies_review_service.repository.ReviewRepository;
import com.reactives_spring.movies_review_service.validator.ReviewValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;

@Component
@RequiredArgsConstructor
public class ReviewHandler {

    private final ReviewRepository repository;
    private final ReviewMapper mapper;
    private final ReviewValidator validator;

    public Mono<ServerResponse> addReview(ServerRequest request) {
        return request.bodyToMono(Review.class)
                .doOnNext(validator::validate)
                .flatMap(repository::save)
                .flatMap(ServerResponse.status(CREATED)::bodyValue);
    }

    public Mono<ServerResponse> getReviews(ServerRequest request) {
        var movieInfoId = request.queryParam("movieInfoId");

        return movieInfoId
                .map(id -> ServerResponse.ok()
                        .body(
                                repository.findByMovieInfoId(Long.valueOf(id)
                                ), Review.class))
                .orElseGet(() -> ServerResponse.ok().body(repository.findAll(), Review.class));
    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {
        var reviewId = request.pathVariable("id");
        return repository.findById(reviewId)
                .switchIfEmpty(Mono.error(new ReviewNotFoundException("review not found")))
                .flatMap(review -> request.bodyToMono(Review.class)
                        .doOnNext(validator::validate)
                        .map(mapper::toReview)
                        .flatMap(repository::save)
                        .flatMap(savedReview -> ServerResponse.ok().bodyValue(savedReview))
                );
    }

    public Mono<ServerResponse> deleteReview(ServerRequest request) {
        var reviewId = request.pathVariable("id");
        var existingReview = repository.findById(reviewId);
        return existingReview
                .flatMap(review -> repository.deleteById(reviewId)
                        .then(ServerResponse.noContent().build()));
    }
}
