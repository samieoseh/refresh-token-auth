package com.samuel.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all paths or adjust as needed
                .allowedOrigins("http://localhost:5173") // Allowed origin
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS") // Include OPTIONS for preflight
                .allowedHeaders("Authorization", "Content-Type", "Origin", "Accept", "X-Requested-With") // Allowedrequest
    // headers
                .allowCredentials(true) // Allows credentials (cookies, etc.)
                .maxAge(3600); // Cache the preflight response for 1 hour
    }
}
