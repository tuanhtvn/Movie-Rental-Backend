package com.rental.movie.component;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.rental.movie.common.AuthProvider;
import com.rental.movie.common.Role;
import com.rental.movie.config.AppConfig;
import com.rental.movie.model.entity.User;
import com.rental.movie.service.TokenService;
import com.rental.movie.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private AppConfig appConfig;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private Encoder encoder;

    private User user = null;
    private String message = "Đăng nhập thành công";
    private Integer status = HttpStatus.OK.value();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String targetUrl = this.appConfig.getClientUrl() + "/oauth2/redirect";

        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        String name = attributes.getOrDefault("name", "").toString();
        String email = attributes.getOrDefault("email", "").toString();
        log.info("Login account: " + email + " - Auth provider: " + AuthProvider.GOOGLE);

        this.userService.getByEmailAndAuthProvider(email, AuthProvider.GOOGLE).ifPresentOrElse(user -> {
            if (user.getIsDeleted() || !user.getIsActive()) {
                if (user.getIsDeleted()) {
                    this.message = "Tài khoản của bạn đã bị xóa khỏi hệ thống";
                    this.status = HttpStatus.NOT_FOUND.value();
                } else {
                    this.message = "Tài khoản của bạn đang bị khóa";
                    this.status = HttpStatus.FORBIDDEN.value();
                }
            } else {
                this.user = user;
            }
        }, () -> {
            User user = new User();
            user.setAuthProvider(AuthProvider.GOOGLE);
            user.setEmail(email);
            user.setIsEmailVerified(true);
            user.setFullName(name);
            user.setRole(Role.USER);
            this.user = this.userService.save(user);
        });
        this.message = this.encoder.encode(this.message);
        if (this.user != null) {
            targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("status", this.status)
                    .queryParam("message", this.message)
                    .queryParam("iduser", this.user.getId())
                    .queryParam("token", this.tokenService.getToken(this.user.getId(), this.user.getRole(), request))
                    .queryParam("fullname", this.encoder.encode(this.user.getFullName()))
                    .queryParam("role", this.user.getRole())
                    .build().toUriString();
            log.info("Login account success: " + email);
        } else {
            targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("status", this.status)
                    .queryParam("message", this.message)
                    .build().toUriString();
            log.error("Login account failed: " + email);
        }
        response.sendRedirect(targetUrl);
    }
}
