package com.rental.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.LoginRequestDTO;
import com.rental.movie.model.dto.RegisterRequestDTO;
import com.rental.movie.model.dto.VerifyRequestDTO;
import com.rental.movie.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        BaseResponse response = BaseResponse.builder()
                .message("Đăng nhập thành công")
                .status(HttpStatus.OK.value())
                .data(authService.login(loginRequestDTO))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        BaseResponse response = BaseResponse.builder()
                .message("Mã xác thực đã được gửi đến email " + registerRequestDTO.getEmail())
                .status(HttpStatus.OK.value())
                .data(authService.register(registerRequestDTO))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/{id}")
    public ResponseEntity<BaseResponse> verifyRegister(@PathVariable("id") String id,
            @RequestBody @Valid VerifyRequestDTO verifyRequestDTO) {
        authService.verifyRegister(id, verifyRequestDTO.getCode());
        BaseResponse response = BaseResponse.builder()
                .message("Đăng ký tài khoản thành công")
                .status(HttpStatus.CREATED.value())
                .build();
        return ResponseEntity.ok(response);
    }

}
