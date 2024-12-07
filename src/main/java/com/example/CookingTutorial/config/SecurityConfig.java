package com.example.CookingTutorial.config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {"/user", "/login", "/introspect", "/user/DKUser","/user/forgotPassword"};

    private final String[] PUT_ENDPOINTS = {"/user/updatePass","/user/updateUser","/user/admin/edit/{userId}", "/user/updateAvatar"};

    private final String[] DELETE_ENDPOINTS = {"/user/admin/delete/{userId}","/post/deletePost/{postId}"};

    private final String[] GET_ENDPOINTS = {"/post/{post_id}", "/post/search", "/post/getAllPost","user/info/{userId}","/post/file/{fileName:.+}","/post/topDislikePost", "/post/getPost/{typePost}"};

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Cấu hình quyền truy cập cho các endpoint
        http.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.PUT,PUT_ENDPOINTS).permitAll()
//                        .requestMatchers(HttpMethod.POST, "/cloudinary/upload", "/post/createPost").permitAll()
                        .requestMatchers(HttpMethod.DELETE,DELETE_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, GET_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/admin/count","/user/getAllUser","/post/{post_id}","/post/search","user/info/{userId}","/post/file/{fileName:.+}").permitAll()
                        .anyRequest().authenticated()
        );


        // Cấu hình CORS
        http.cors(httpSecurityCorsConfigurer -> corsConfigurationSource());

        // Xác thực JWT
        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        // Tắt CSRF (tùy chọn, nếu bạn không cần bảo vệ CSRF trong trường hợp này)
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // Cấu hình converter cho role từ JWT
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    // Cấu hình JWT decoder
    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    // Cấu hình CORS
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:5173")); // Thêm URL frontend của bạn
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Content-Type", "Authorization", "*"));
        corsConfiguration.setAllowCredentials(true);  // Nếu frontend cần gửi cookies hoặc thông tin đăng nhập

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);  // Áp dụng cho tất cả các endpoint
        return source;
    }
}
