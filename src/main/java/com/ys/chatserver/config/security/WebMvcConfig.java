package com.ys.chatserver.config.security;

import com.ys.chatserver.config.properties.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final CorsProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
        .allowedOrigins(corsProperties.getAllowedOrigins().split(","))
        .allowedMethods(corsProperties.getAllowedMethods().split(","))
        .allowedHeaders(corsProperties.getAllowedHeaders().split(","))
        .allowCredentials(true)
        .maxAge(corsProperties.getMaxAge());
    }
}
