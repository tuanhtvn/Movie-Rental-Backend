package com.rental.movie.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.exception.CustomException;

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

    // Authentication Error
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<BaseResponse> handleInsufficientAuthenticationException(Exception exception) {
        log.error("Exception: " + exception.getMessage());
        BaseResponse baseResponse = new BaseResponse();
        String message = "Yêu cầu của bạn không được xác thực. Vui lòng đăng nhập và thử lại.";
        baseResponse.setMessage(message);
        baseResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        baseResponse.setData(null);
        return ResponseEntity.status(baseResponse.getStatus()).body(baseResponse);
    }

    // Validation Error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        log.error("Exception: " + exception.getMessage());
        BaseResponse baseResponse = new BaseResponse();
        String message = exception.getBindingResult().getFieldError().getDefaultMessage();
        baseResponse.setMessage(message);
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        baseResponse.setData(null);
        return ResponseEntity.status(baseResponse.getStatus()).body(baseResponse);
    }
}
