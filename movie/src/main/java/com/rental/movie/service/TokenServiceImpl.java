package com.rental.movie.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.google.common.hash.Hashing;
import com.rental.movie.common.IAuthentication;
import com.rental.movie.common.Role;
import com.rental.movie.common.TokenResponse;
import com.rental.movie.config.AppConfig;
import com.rental.movie.exception.CustomException;
import com.rental.movie.model.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Autowired
    private AppConfig appConfig;
    @Lazy
    @Autowired
    private JwtEncoder jwtEncoder;
    @Lazy
    @Autowired
    private JwtDecoder jwtDecoder;
    @Lazy
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private IAuthentication authManager;

    @Override
    public TokenResponse getToken(String userId, Role role, HttpServletRequest request) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(appConfig.getExpiresTime(), ChronoUnit.DAYS);

        log.debug("Token created for user: {}", userId);
        log.debug("Role: {}", role);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(userId)
                .claim("role", role)
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        if (role.equals(Role.USER)) {
            deviceService.add(userId, hashString(token), request, expiresAt, now);
        }
        return TokenResponse.builder()
                .token(token)
                .expiredAt(expiresAt)
                .build();
    }

    @Override
    public Boolean compareTokens(String tokenHash, String tokenPlain) {
        return tokenHash.equals(hashString(tokenPlain));

    }

    private String hashString(String str) {
        return Hashing.sha256()
                .hashString(str, StandardCharsets.UTF_8)
                .toString();
    }

    @Override
    public Boolean checkToken(String token) {
        User user = authManager.getUserAuthentication();
        if (user == null) {
            return false;
        }
        if (!user.getRole().equals(Role.USER)) {
            return true;
        }
        return user.getDevices().stream()
                .anyMatch(d -> d.getToken().equals(hashString(token)));
    }

    @Override
    public String getToken(String filmId) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(1, ChronoUnit.DAYS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(filmId)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public void validateToken(String token, String filmId) {
        try {
            Jwt decodedJwt = jwtDecoder.decode(token);
            if (!decodedJwt.getSubject().equals(filmId)) {
                throw new CustomException("Token không hợp lệ", HttpStatus.UNAUTHORIZED.value());
            }
        } catch (JwtException e) {
            throw new CustomException("Token không hợp lệ", HttpStatus.UNAUTHORIZED.value());
        }
    }

    @Override
    public void validateToken(String token) {
        try {
            Jwt decodedJwt = jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new CustomException("Token không hợp lệ", HttpStatus.UNAUTHORIZED.value());
        }
    }
}
