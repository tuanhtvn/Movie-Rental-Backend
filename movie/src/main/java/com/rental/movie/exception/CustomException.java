package com.rental.movie.exception;

public class CustomException extends RuntimeException {
    private Integer statusCode;

    public CustomException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
