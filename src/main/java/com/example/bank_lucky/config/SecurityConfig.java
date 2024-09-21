package com.example.bank_lucky.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Отключите CSRF, если это необходимо
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register", "/login","/").permitAll() // Разрешаем доступ всем к /register и /login
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .httpBasic(); // Или другой метод аутентификации, если нужен

        return http.build();
    }
}