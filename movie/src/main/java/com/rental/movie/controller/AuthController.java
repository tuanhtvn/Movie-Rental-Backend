package com.rental.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.movie.common.BaseResponse;
import com.rental.movie.model.dto.ForgotPasswordRequestDTO;
import com.rental.movie.model.dto.LoginRequestDTO;
import com.rental.movie.model.dto.RegisterRequestDTO;
import com.rental.movie.model.dto.VerifyRequestDTO;
import com.rental.movie.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api")
@Tag(name = "Auth Controller")
public class AuthController {
        @Autowired
        private AuthService authService;

        @Operation(summary = "Đăng nhập", description = "API đăng nhập vào hệ thống")
        @ApiResponse(responseCode = "200", description = "Đăng nhập thành công")
        @PostMapping("/auth/login")
        public ResponseEntity<BaseResponse> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO,
                        HttpServletRequest request) {
                BaseResponse response = BaseResponse.builder()
                                .message("Đăng nhập thành công")
                                .status(HttpStatus.OK.value())
                                .data(authService.login(loginRequestDTO, request))
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Đăng ký tài khoản - Bước 1", description = "API đăng ký tài khoản")
        @ApiResponse(responseCode = "200", description = "Mã xác thực đã được gửi đến email")
        @PostMapping("/auth/register")
        public ResponseEntity<BaseResponse> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
                BaseResponse response = BaseResponse.builder()
                                .message("Mã xác thực đã được gửi đến email " + registerRequestDTO.getEmail())
                                .status(HttpStatus.OK.value())
                                .data(authService.register(registerRequestDTO))
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Đăng ký tài khoản - Bước 2", description = "API xác thực đăng ký tài khoản")
        @ApiResponse(responseCode = "201", description = "Đăng ký tài khoản thành công")
        @PostMapping("/auth/register/{id}")
        public ResponseEntity<BaseResponse> verifyRegister(@PathVariable("id") String id,
                        @RequestBody @Valid VerifyRequestDTO verifyRequestDTO) {
                authService.verify(id, verifyRequestDTO, true);
                BaseResponse response = BaseResponse.builder()
                                .message("Đăng ký tài khoản thành công")
                                .status(HttpStatus.CREATED.value())
                                .build();
                return ResponseEntity.status(response.getStatus()).body(response);
        }

        @Operation(summary = "Quên mật khẩu - Bước 1", description = "API quên mật khẩu")
        @ApiResponse(responseCode = "200", description = "Mã xác thực đã được gửi đến email")
        @PutMapping("/auth/forgot-password")
        public ResponseEntity<BaseResponse> forgotPassword(
                        @RequestBody @Valid ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
                BaseResponse response = BaseResponse.builder()
                                .message("Mã xác thực đã được gửi đến email " + forgotPasswordRequestDTO.getEmail())
                                .status(HttpStatus.OK.value())
                                .data(authService.forgotPassword(forgotPasswordRequestDTO))
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Quên mật khẩu - Bước 2", description = "API xác thực quên mật khẩu")
        @ApiResponse(responseCode = "200", description = "Đặt lại mật khẩu thành công")
        @PutMapping("/auth/forgot-password/{id}")
        public ResponseEntity<BaseResponse> verifyForgotPassword(@PathVariable("id") String id,
                        @RequestBody @Valid VerifyRequestDTO verifyRequestDTO) {
                authService.verify(id, verifyRequestDTO, false);
                BaseResponse response = BaseResponse.builder()
                                .message("Đặt lại mật khẩu thành công")
                                .status(HttpStatus.OK.value())
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Đăng xuất", description = "API đăng xuất khỏi hệ thống")
        @ApiResponse(responseCode = "200", description = "Đăng xuất thành công")
        @GetMapping("/logout")
        public ResponseEntity<BaseResponse> logout(HttpServletRequest request) {
                String token = request.getHeader("Authorization");
                authService.logout(token.substring(7));
                BaseResponse response = BaseResponse.builder()
                                .message("Đăng xuất thành công")
                                .status(HttpStatus.OK.value())
                                .build();
                return ResponseEntity.ok(response);
        }

}
