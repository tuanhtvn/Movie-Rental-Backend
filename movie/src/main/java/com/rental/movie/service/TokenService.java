package com.rental.movie.service;

import com.rental.movie.common.Role;

public interface TokenService {
    public String getToken(String userId, Role role);
}
