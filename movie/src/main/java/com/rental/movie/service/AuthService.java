package com.rental.movie.service;

import com.rental.movie.model.dto.ForgotPasswordRequestDTO;
import com.rental.movie.model.dto.LoginRequestDTO;
import com.rental.movie.model.dto.LoginResponseDTO;
import com.rental.movie.model.dto.RegisterRequestDTO;
import com.rental.movie.model.dto.VerifyRequestDTO;
import com.rental.movie.model.dto.VerifyResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO, HttpServletRequest request);

    public VerifyResponseDTO register(RegisterRequestDTO registerRequestDTO);

    public void verify(String id, VerifyRequestDTO verifyRequestDTO, Boolean isRegister);

    public VerifyResponseDTO forgotPassword(ForgotPasswordRequestDTO forgotPasswordRequestDTO);

}
