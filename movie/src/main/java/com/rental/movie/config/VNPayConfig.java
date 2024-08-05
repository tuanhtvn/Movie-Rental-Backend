package com.rental.movie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VNPayConfig {
    @Value("${vnp.TmnCode}")
    private String tmnCode;

    @Value("${vnp.HashSecret}")
    private String hashSecret;

    @Value("${vnp.Url}")
    private String vnpUrl;

    public String getTmnCode() {
        return tmnCode;
    }

    public String getHashSecret() {
        return hashSecret;
    }

    public String getVnpUrl() {
        return vnpUrl;
    }
}
