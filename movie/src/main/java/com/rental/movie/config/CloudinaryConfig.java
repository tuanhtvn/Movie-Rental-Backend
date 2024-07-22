package com.rental.movie.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
    @Autowired
    private AppConfig appConfig;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", appConfig.getCloudName());
        config.put("api_key", appConfig.getApiKey());
        config.put("api_secret", appConfig.getApiSecret());
        return new Cloudinary(config);
    }
}
