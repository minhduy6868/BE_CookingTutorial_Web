package com.example.CookingTutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // Địa chỉ frontend của bạn
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Bao gồm OPTIONS
                .allowedHeaders("Content-Type", "Authorization", "*") // Cho phép headers cụ thể như Content-Type
                .allowCredentials(true); // Nếu bạn cần truyền cookies hoặc thông tin đăng nhập
    }
}
