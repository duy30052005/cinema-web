package com.example.demo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Cho phép tất cả các đường dẫn
                .allowedOrigins("https://app-cinema.vercel.app") // Link Frontend Vercel
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Các method cho phép
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}