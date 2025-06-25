package com.techcmr.tech_cmr.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.micrometer.common.lang.NonNull;

// Classi di configurazione che implementa WebMvcConfigurer
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Implementiamo il metodo che ci serve per stabilire la politica Cors
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET","POST","PUT","DELETE")
                .allowedHeaders("*");
    }
}