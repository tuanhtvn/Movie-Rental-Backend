package com.rental.movie.service;

public interface TokenService {
    public String getToken(String userId, String role);
}
