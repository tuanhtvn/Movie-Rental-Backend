package com.rental.movie.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.rental.movie.common.Role;
import com.rental.movie.config.AppConfig;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Autowired
    private AppConfig appConfig;
    @Lazy
    @Autowired
    private JwtEncoder jwtEncoder;

    @Override
    public String getToken(String userId, Role role) {
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

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
