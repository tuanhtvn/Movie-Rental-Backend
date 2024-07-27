package com.rental.movie.service;

import com.rental.movie.common.Role;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {
    public String getToken(String userId, Role role, HttpServletRequest request);
}
