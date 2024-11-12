package com.example.CookingTutorial.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@CrossOrigin
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
