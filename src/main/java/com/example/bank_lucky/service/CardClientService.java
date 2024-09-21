package com.example.bank_lucky.service;

import com.example.bank_lucky.dto.ClientDto;
import com.example.bank_lucky.request.LoginRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardClientService {
    @Value("${authentication.service.url}")
    private String url;

    private final RestTemplate restTemplate;



    public String loginClient(LoginRequest loginRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
            log.debug("Sending login request to URL: {}", url + "/api/clients/login");
            log.debug("Request body: {}", loginRequest);

            ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
                    url + "/api/clients/login",
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, String>>() {}

            );
            log.debug("Received response status: {}", responseEntity.getStatusCode());
            log.debug("Received response body: {}", responseEntity.getBody());

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, String> responseBody = responseEntity.getBody();
                return responseBody != null ? responseBody.get("message") : "Login successful";
            } else {
                throw new RuntimeException("Login failed: " + responseEntity.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("Error during login: {}", e.getMessage(), e);

            throw new RuntimeException("Login failed", e);
        }
    }

    public String registerClient(ClientDto clientDto) {
        ResponseEntity<ClientDto> response = restTemplate.postForEntity(url + "/api/clients/register", clientDto, ClientDto.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return "Client registered successfully";
        } else {
            throw new RuntimeException("Error during registration: " + response.getStatusCode());
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
