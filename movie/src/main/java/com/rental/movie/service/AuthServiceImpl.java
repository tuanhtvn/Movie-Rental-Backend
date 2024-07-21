package com.rental.movie.service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.common.Role;
import com.rental.movie.config.AppConfig;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.dto.LoginRequestDTO;
import com.rental.movie.model.dto.LoginResponseDTO;
import com.rental.movie.model.dto.RegisterRequestDTO;
import com.rental.movie.model.dto.RegisterResponseDTO;
import com.rental.movie.model.entity.User;
import com.rental.movie.model.entity.Verify;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private ModelMapper modelMapper;

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
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        log.info("Register account: " + registerRequestDTO.getEmail() + " - Auth provider: " + AuthProvider.LOCAL);
        registerRequestDTO.setEmail(registerRequestDTO.getEmail().toLowerCase());
        if (!registerRequestDTO.getPassword().equals(registerRequestDTO.getPasswordConfirm())) {
            log.error("Password and confirm password are not matched: " +
                    registerRequestDTO.getEmail());
            throw new CustomException("Mật khẩu và xác nhận mật khẩu không khớp",
                    HttpStatus.BAD_REQUEST.value());
        }
        User user = userService.getByEmailAndAuthProvider(registerRequestDTO.getEmail(),
                AuthProvider.LOCAL).orElse(null);
        String id = (user != null) ? user.getId() : null;
        if (user != null) {
            if (user.getIsDeleted()) {
                log.error("User is deleted: " + registerRequestDTO.getEmail());
                throw new CustomException("Email đăng ký đã bị chặn",
                        HttpStatus.BAD_REQUEST.value());
            } else if (user.getIsEmailVerified()) {
                log.error("User is already existed: " + registerRequestDTO.getEmail());
                throw new CustomException("Email đăng ký đã tồn tại",
                        HttpStatus.BAD_REQUEST.value());
            }
        }
        user = modelMapper.map(registerRequestDTO, User.class);
        if (id != null) {
            user.setId(id);
        }
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setPassword(BCrypt.hashpw(registerRequestDTO.getPassword(), BCrypt.gensalt(appConfig.getLogRounds())));
        user.setRole(Role.USER);
        String verifyCode = getVerifyCode();
        user.setVerify(new Verify(BCrypt.hashpw(verifyCode, BCrypt.gensalt(appConfig.getLogRounds())),
                ZonedDateTime.now().plus(appConfig.getVerifyExpireTime(), ChronoUnit.MINUTES)));
        user = userService.save(user);
        mailService.sendMailVerify(user.getEmail(), user.getFullName(), verifyCode);
        return RegisterResponseDTO.builder()
                .id(user.getId())
                .expiredAt(user.getVerify().getExpiredAt())
                .build();
    }

    @Override
    public void verifyRegister(String id, String code) {
        log.info("Verify account: " + id);
        User user = userService.getById(id)
                .orElseThrow(() -> {
                    log.error("User not found: {}", id);
                    throw new CustomException("Tài khoản của bạn không tồn tại",
                            HttpStatus.NOT_FOUND.value());
                });
        if (user.getVerify() == null) {
            log.error("Verification code for user not found: {}", id);
            throw new CustomException("Mã xác thực không tồn tại",
                    HttpStatus.BAD_REQUEST.value());
        }
        verifyCode(code, user);
        user.setIsEmailVerified(true);
        user.setVerify(null);
        userService.save(user);
    }

    //
    private String getVerifyCode() {
        StringBuffer verifyCode = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            verifyCode.append(random.nextInt(10));
        }
        return verifyCode.toString();
    }

    private void verifyCode(String code, User user) {
        if (BCrypt.checkpw(code, user.getVerify().getCode())) {
            if (user.getVerify().getExpiredAt().isBefore(ZonedDateTime.now())) {
                log.error("Verify code is expired: " + user.getEmail());
                throw new CustomException("Mã xác thực đã hết hạn",
                        HttpStatus.BAD_REQUEST.value());
            }
        } else {
            log.error("Verify code is incorrect: " + user.getEmail());
            throw new CustomException("Mã xác thực không chính xác",
                    HttpStatus.BAD_REQUEST.value());
        }
    }
}
