package com.rental.movie.exception.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.exception.CustomException;
import com.rental.movie.exception.TokenAuthenticationException;

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

    // Token Error
    @ExceptionHandler({ AuthenticationException.class, JwtException.class })
    public ResponseEntity<BaseResponse> handleNimbusJwtDecoderException(Exception exception) {
        log.error("Exception: " + exception.getMessage());
        BaseResponse baseResponse = new BaseResponse();
        String message = "Thông tin xác thực không hợp lệ hoặc đã hết hạn. Vui lòng đăng nhập và thử lại.";
        baseResponse.setMessage(message);
        baseResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        baseResponse.setData(null);
        return ResponseEntity.status(baseResponse.getStatus()).body(baseResponse);
    }

    // Access Denied Error
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse> handleAccessDeniedException(Exception exception) {
        log.error("Exception: " + exception.getMessage());
        BaseResponse baseResponse = new BaseResponse();
        String message = "Bạn không có quyền truy cập vào tài nguyên này.";
        baseResponse.setMessage(message);
        baseResponse.setStatus(HttpStatus.FORBIDDEN.value());
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

    // Token Authentication Error
    @ExceptionHandler(TokenAuthenticationException.class)
    public ResponseEntity<BaseResponse> handleTokenAuthenticationException(TokenAuthenticationException exception) {
        log.error("Exception: " + exception.getMessage());
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessage(exception.getMessage());
        baseResponse.setStatus(exception.getStatusCode());
        baseResponse.setData(null);
        return ResponseEntity.status(baseResponse.getStatus()).body(baseResponse);
    }

    // Not Found Error
    @ExceptionHandler(IOException.class)
    public ResponseEntity<BaseResponse> handleIOException(IOException e) {
        log.error("Exception: " + e.getMessage());
        BaseResponse response = new BaseResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage("Không tìm thấy tài nguyên");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Max Upload Size Error
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<BaseResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("Exception: " + e.getMessage());
        BaseResponse response = new BaseResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Kích thước tệp tải lên vượt quá giới hạn cho phép");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
