package com.example.CookingTutorial.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {


    @Bean
    public Cloudinary getCloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dz22gpipd");
        config.put("api_key", "816781279367396");
        config.put("api_secret", "4Yct2Y7U64h2DlUbtEm0ctRvhIM");
        config.put("secure", "true");
        return new Cloudinary(config);
    }
}
