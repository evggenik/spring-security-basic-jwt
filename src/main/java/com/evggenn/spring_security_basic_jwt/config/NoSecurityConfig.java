package com.evggenn.spring_security_basic_jwt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("test")
public class NoSecurityConfig {

    @Bean
    public SecurityFilterChain noSecurityConfigure(HttpSecurity http) throws Exception {

        return http
                .csrf(customizer->customizer.disable())
                .authorizeHttpRequests(request->request
                        .anyRequest().permitAll())
                        .build();

    }
}
