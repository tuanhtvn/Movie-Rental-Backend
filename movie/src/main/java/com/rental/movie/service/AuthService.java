package com.rental.movie.service;

import com.rental.movie.model.dto.LoginRequestDTO;
import com.rental.movie.model.dto.LoginResponseDTO;

public interface AuthService {
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    public String register();

    public void logout();
}
