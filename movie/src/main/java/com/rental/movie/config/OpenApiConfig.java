package com.rental.movie.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
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

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title(this.title)
                .version(this.version)
                .description(this.description))
                .servers(List.of(new Server().url(this.hostServerUrl).description("Host server"),
                        new Server().url(this.localServerUrl).description("Local server")));
    }
}
