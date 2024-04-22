package com.oyatrij.springoauthsociallogin.config;

import com.oyatrij.springoauthsociallogin.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configurable
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authz -> authz
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/user").hasRole("USER")
                        .anyRequest().authenticated())
                .exceptionHandling(exha -> exha.accessDeniedPage("/accessDenied"));

        http.logout(logout -> logout.logoutUrl("/logout")
                .logoutSuccessUrl("/").permitAll());

        http.oauth2Login(oauth2config -> oauth2config.loginPage("/login")
                .userInfoEndpoint(userInfoEndpointConfig ->
                    userInfoEndpointConfig.userService(customOAuth2UserService)));

        return http.build();
    }
}
