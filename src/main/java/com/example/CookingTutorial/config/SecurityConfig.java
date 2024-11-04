package com.example.CookingTutorial.config;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfig {
    final String JWT_SECRET = "";

    String generateToken(){
        return "";
    }

}
