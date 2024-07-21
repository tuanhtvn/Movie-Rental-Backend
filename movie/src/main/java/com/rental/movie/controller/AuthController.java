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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/auth")
@Tag(name = "Auth Controller")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "Đăng nhập", description = "API đăng nhập vào hệ thống")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Đăng nhập thành công"))
    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        BaseResponse response = BaseResponse.builder()
                .message("Đăng nhập thành công")
                .status(HttpStatus.OK.value())
                .data(authService.login(loginRequestDTO))
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Đăng ký tài khoản - Bước 1", description = "API đăng ký tài khoản")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Mã xác thực đã được gửi đến email"))
    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        BaseResponse response = BaseResponse.builder()
                .message("Mã xác thực đã được gửi đến email " + registerRequestDTO.getEmail())
                .status(HttpStatus.OK.value())
                .data(authService.register(registerRequestDTO))
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Đăng ký tài khoản - Bước 2", description = "API xác thực đăng ký tài khoản")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "Đăng ký tài khoản thành công"))
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
