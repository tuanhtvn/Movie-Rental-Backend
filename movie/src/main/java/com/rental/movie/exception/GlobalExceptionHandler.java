package com.rental.movie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rental.movie.common.BaseResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleException(Exception e) {
        log.error("Exception: " + e.getMessage());
        BaseResponse response = new BaseResponse();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Internal Server Error");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Custom Exception Error
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponse> handleCustomException(CustomException e) {
        log.error("Custom Exception: " + e.getMessage());
        BaseResponse response = new BaseResponse();
        response.setStatus(e.getStatusCode());
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
