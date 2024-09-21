package com.example.bank_lucky.config;

import com.example.bank_lucky.dto.ClientDto;
import com.example.bank_lucky.request.LoginRequest;
import com.example.bank_lucky.service.CardClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CardClientService userFeignClient;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(CardClientService userFeignClient) {
        this.userFeignClient = userFeignClient;
        this.passwordEncoder = new BCryptPasswordEncoder(); // Инициализация PasswordEncoder
    }

//@Override
//public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//    String email = authentication.getName(); // Получаем email
//    String password = (String) authentication.getCredentials();
//
//    try {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail(email); // Устанавливаем email
//        loginRequest.setPassword(password); // Устанавливаем пароль
//
//        // Используем метод из CardClientService для аутентификации
//        String message = userFeignClient.loginClient(loginRequest); // Теперь возвращает строку
//
//        // Здесь вы можете добавить логику для проверки сообщения,
//        // например, если оно говорит об успешном логине.
//        if (message.startsWith("Login successful")) {
//            // Здесь можно создать UserDetails и вернуть Authentication
//            // Например, создайте UserDetails из email или username
//            UserDetails userDetails = org.springframework.security.core.userdetails.User
//                    .withUsername(email) // Используем email как username
//                    .password(password) // В этом случае можно не хранить зашифрованный пароль
//                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))) // Укажите нужные роли
//                    .build();
//
//            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
//        } else {
//            throw new BadCredentialsException(message); // Используйте сообщение из сервиса как ошибку
//        }
//    } catch (Exception e) {
//        log.error("Authentication failed for user: {}", email, e);
//        throw new BadCredentialsException("Authentication failed", e);
//    }
//}
@Override
public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = authentication.getName();
    String password = (String) authentication.getCredentials();

    try {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        String message = userFeignClient.loginClient(loginRequest);

        if ("Login successful".equals(message)) {
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(email)
                    .password("") // Не храним пароль в UserDetails
                    .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                    .build();

            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }

    } catch (Exception e) {
        log.error("Unexpected error during authentication for user: {}. Error: {}", email, e.getMessage());
        throw new AuthenticationServiceException("Unexpected authentication error", e);
    }
}
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}