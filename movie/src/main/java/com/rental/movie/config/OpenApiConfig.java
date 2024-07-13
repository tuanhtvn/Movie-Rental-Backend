package com.rental.movie.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {
    @Autowired
    private AppConfig appConfig;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title(this.appConfig.getTitle())
                .version(this.appConfig.getVersion())
                .description(this.appConfig.getDescription()))
                .servers(List.of(new Server().url(this.appConfig.getHostServerUrl()).description("Host server"),
                        new Server().url(this.appConfig.getLocalServerUrl()).description("Local server")))
                .components(
                        new Components()
                                .addSecuritySchemes("cookie", new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("cookie")))
                .security(List.of(new SecurityRequirement().addList("cookie")));
    }
}
