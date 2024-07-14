package com.rental.movie.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.rental.movie.config.AppConfig;

public class TokenServiceImpl implements TokenService {
    
    @Autowired
    private AppConfig appConfig;

    @Override
    public String getToken(String userId, String role) {
        return null;
    }
}
