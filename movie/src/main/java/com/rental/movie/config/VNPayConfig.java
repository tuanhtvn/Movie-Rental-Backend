package com.rental.movie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class VNPayConfig {
    @Value("${vnp.TmnCode}")
    private String tmnCode;

    @Value("${vnp.HashSecret}")
    private String hashSecret;

    @Value("${vnp.Url}")
    private String vnpUrl;

    @Value("${vnp.ReturnUrl}")
    private String vnpReturnUrl;

    public String getTmnCode() {
        return tmnCode;
    }

    public String getHashSecret() {
        return hashSecret;
    }

    public String getVnpUrl() {
        return vnpUrl;
    }

    public String getVnpReturnUrl() {
        return vnpReturnUrl;
    }
}
