package com.reactives_spring.movies_info_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleRequestBodyError(WebExchangeBindException ex) {
        log.error("Exception caught in handler : {}", ex.getMessage(), ex);
        var error = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .collect(joining(","));
        log.error("Error is : {}", error);
        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InfoNotFountException.class)
    public ResponseEntity<String> handleNotFoundError(InfoNotFountException ex) {
        return ResponseEntity.status(NOT_FOUND).body(ex.getMessage());
    }
}
