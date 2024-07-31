package com.rental.movie.component;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rental.movie.exception.TokenAuthenticationException;
import com.rental.movie.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Order(1)
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("TokenFilter");
        String token = request.getHeader("Authorization");
        if (token == null) {
            log.info("Token is null");
            filterChain.doFilter(request, response);
            return;
        }
        token = token.substring(7);
        log.debug("Token: {}", token);
        if (!tokenService.checkToken(token)) {
            log.info("Token is invalid");
            throw new TokenAuthenticationException("Thông tin xác thực không hợp lệ, vui lòng đăng nhập và thử lại",
                    HttpStatus.UNAUTHORIZED.value());
        }
        filterChain.doFilter(request, response);
    }
}
