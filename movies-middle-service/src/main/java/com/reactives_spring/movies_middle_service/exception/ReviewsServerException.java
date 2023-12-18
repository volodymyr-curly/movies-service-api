package com.reactives_spring.movies_middle_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewsServerException extends RuntimeException{

    private String message;

    public ReviewsServerException(String message) {
        super(message);
        this.message = message;
    }
}
