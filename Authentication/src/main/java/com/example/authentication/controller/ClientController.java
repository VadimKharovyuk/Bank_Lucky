package com.example.authentication.controller;

import com.example.authentication.Request.LoginRequest;
import com.example.authentication.dto.ClientDto;
import com.example.authentication.maper.ClientMapper;
import com.example.authentication.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginClient(@RequestBody LoginRequest loginRequest) {
        log.info("Received login request for email: {}", loginRequest.getEmail());

        try {
            UserDetails userDetails = clientService.loadUserByUsername(loginRequest.getEmail());

            if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                log.info("Login successful for email: {}", loginRequest.getEmail());
                return ResponseEntity.ok(Collections.singletonMap("message", "Login successful"));
            } else {
                log.warn("Invalid password for email: {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", "Invalid credentials"));
            }
        } catch (UsernameNotFoundException e) {
            log.warn("User not found for email: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Invalid credentials"));
        } catch (Exception e) {
            log.error("Unexpected error during login for email: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "An unexpected error occurred"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            ClientDto clientDto = clientService.getClientById(id);
            return ResponseEntity.ok(clientDto);
        } catch (RuntimeException e) {
            log.warn("Client with id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client not found with id: " + id);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ClientDto> registerClient(@RequestBody ClientDto clientDto) {
        log.info("Registering new client: {}", clientDto.getEmail());
        ClientDto savedClient = ClientMapper.toDto(clientService.saveClient(clientDto));
        return ResponseEntity.ok(savedClient);
    }



    @GetMapping("/email")
    public ResponseEntity<ClientDto> getByEmail(@RequestParam String email) {
        try {
            ClientDto clientDto = clientService.findByEmail(email);
            return ResponseEntity.ok(clientDto); // Возвращаем найденного клиента с HTTP статусом 200
        } catch (UsernameNotFoundException e) {
            log.warn("Client not found with email: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Возвращаем статус 404, если клиент не найден
        }
    }


}