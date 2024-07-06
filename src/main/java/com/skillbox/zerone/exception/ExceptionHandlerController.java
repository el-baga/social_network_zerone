package com.skillbox.zerone.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler({BadRequestException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<BadRequestRs> handleException(Exception e) {
        BadRequestRs badRequestRs = BadRequestRs.builder()
                .error("BadRequestException")
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getMessage())
                .build();
        return new ResponseEntity<>(badRequestRs, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestRs> handleValidationException(MethodArgumentNotValidException ex) {
        String cause = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        log.error(ex.getMessage());
        BadRequestRs badRequestRs = BadRequestRs.builder()
                .error("BadRequestException")
                .timestamp(System.currentTimeMillis())
                .errorDescription(cause)
                .build();
        return new ResponseEntity<>(badRequestRs, HttpStatus.BAD_REQUEST);
    }
}
