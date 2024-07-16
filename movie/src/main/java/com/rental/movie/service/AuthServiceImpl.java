package com.rental.movie.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.LoginRequestDTO;
import com.rental.movie.model.dto.LoginResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        log.info("Login account: " + loginRequestDTO.getEmail() + " - Auth provider: " + AuthProvider.LOCAL);
        loginRequestDTO.setEmail(loginRequestDTO.getEmail().toLowerCase());
        return userService.getByEmailAndAuthProvider(loginRequestDTO.getEmail(), AuthProvider.LOCAL)
                .map(user -> {
                    // check status of user
                    if (user.getIsDeleted() || !user.getIsEmailVerified()) {
                        log.error("User is deleted or email is not verified: " + loginRequestDTO.getEmail());
                        throw new CustomException("Tài khoản của bạn không tồn tại hoặc đã bị xóa",
                                HttpStatus.NOT_FOUND.value());
                    }
                    if (!user.getIsActive()) {
                        log.error("User is not active: " + loginRequestDTO.getEmail());
                        throw new CustomException("Tài khoản của bạn đang bị khóa",
                                HttpStatus.FORBIDDEN.value());
                    }
                    // check password
                    if (BCrypt.checkpw(loginRequestDTO.getPassword(), user.getPassword())) {
                        log.info("Login success: " + loginRequestDTO.getEmail());
                        return LoginResponseDTO.builder()
                                .token(tokenService.getToken(user.getId(), user.getRole()))
                                .fullName(user.getFullName())
                                .role(user.getRole().name())
                                .build();
                    } else {
                        log.error("Password is incorrect: " + loginRequestDTO.getEmail());
                        throw new CustomException("Tài khoản hoặc mật khẩu không chính xác",
                                HttpStatus.UNAUTHORIZED.value());
                    }
                })
                .orElseThrow(() -> {
                    log.error("User not found: " + loginRequestDTO.getEmail());
                    throw new CustomException("Tài khoản hoặc mật khẩu không chính xác",
                            HttpStatus.UNAUTHORIZED.value());
                });
    }

    @Override
    public String register() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

    @Override
    public void logout() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

}
