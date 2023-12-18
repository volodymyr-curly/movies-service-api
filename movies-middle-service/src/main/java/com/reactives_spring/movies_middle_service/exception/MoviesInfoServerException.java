package com.reactives_spring.movies_middle_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoviesInfoServerException extends RuntimeException{

    private String message;

    public MoviesInfoServerException(String message) {
        super(message);
        this.message = message;
    }
}
