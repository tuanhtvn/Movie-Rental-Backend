package com.rental.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.LoginRequestDTO;
import com.rental.movie.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@Valid LoginRequestDTO loginRequestDTO) {
        BaseResponse response = BaseResponse.builder()
                .message("Đăng nhập thành công")
                .status(HttpStatus.OK.value())
                .data(authService.login(loginRequestDTO))
                .build();
        return ResponseEntity.ok(response);

    }
}
