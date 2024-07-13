package com.rental.movie.config;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class AppConfig {
    @Value("${client.url}")
    private String clientUrl;

    @Value("${spring.data.mongodb.uri}")
    private String uri;
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.jwt.rsa.public-key}")
    private RSAPublicKey publicKey;
    @Value("${spring.jwt.rsa.private-key}")
    private RSAPrivateKey privateKey;
    @Value("${spring.cookie.expiresTime}")
    private Integer expiresTime;

    @Value("${open.api.title}")
    private String title;
    @Value("${open.api.version}")
    private String version;
    @Value("${open.api.description}")
    private String description;
    @Value("${open.api.local-server-url}")
    private String localServerUrl;
    @Value("${open.api.host-server-url}")
    private String hostServerUrl;
}
