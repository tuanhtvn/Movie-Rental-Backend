package com.rental.movie.service;

import com.rental.movie.model.dto.LoginRequestDTO;
import com.rental.movie.model.dto.LoginResponseDTO;
import com.rental.movie.model.dto.RegisterRequestDTO;
import com.rental.movie.model.dto.RegisterResponseDTO;

public interface AuthService {
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO);

    public void verifyRegister(String id, String code);

}
