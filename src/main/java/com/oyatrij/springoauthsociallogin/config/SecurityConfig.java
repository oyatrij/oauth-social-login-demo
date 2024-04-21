package com.oyatrij.springoauthsociallogin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //SpringSecurity 6

        // CORS 설정
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        cors.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        cors.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        cors.setAllowCredentials(true);

        // CSRF 설정
        http.csrf(Customizer.withDefaults());

        return http.build();
    }
}
