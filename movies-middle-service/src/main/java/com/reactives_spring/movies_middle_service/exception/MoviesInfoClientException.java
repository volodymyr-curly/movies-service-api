package com.reactives_spring.movies_middle_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoviesInfoClientException extends RuntimeException{

    private String message;
    private Integer statusCode;

    public MoviesInfoClientException(String message, Integer statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }
}
