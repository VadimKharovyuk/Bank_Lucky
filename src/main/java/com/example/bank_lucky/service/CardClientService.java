package com.example.bank_lucky.service;

import com.example.bank_lucky.dto.ClientDto;
import com.example.bank_lucky.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CardClientService {
    @Value("${authentication.service.url}")
    private String url;

    private final RestTemplate restTemplate;


    public String registerClient(ClientDto clientDto) {
        ResponseEntity<ClientDto> response = restTemplate.postForEntity(url + "/api/clients/register", clientDto, ClientDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return "Client registered successfully";
        } else {
            throw new RuntimeException("Error during registration: " + response.getStatusCode());
        }
    }

    public String loginClient(LoginRequest loginRequest) {
        ResponseEntity<String> response = restTemplate.postForEntity(url + "/api/clients/login", loginRequest, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // Возвращает сообщение о успешном логине
        } else {
            throw new RuntimeException("Login failed: " + response.getStatusCode());
        }
    }

    public ClientDto getClientById(Long id) {
        ResponseEntity<ClientDto> response = restTemplate.getForEntity(url + "/api/clients/" + id, ClientDto.class);
        return response.getBody();
    }

    public ClientDto getClientByEmail(String email) {
        ResponseEntity<ClientDto> response = restTemplate.getForEntity(url + "/api/clients/email?email=" + email, ClientDto.class);
        return response.getBody();
    }
}
