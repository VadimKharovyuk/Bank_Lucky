package com.example.authentication.service;

import com.example.authentication.dto.ClientDto;
import com.example.authentication.maper.ClientMapper;
import com.example.authentication.model.Client;
import com.example.authentication.model.Role;
import com.example.authentication.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService implements UserDetailsService {


    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;


    public Client saveClient(ClientDto clientDto) {
        Client client = ClientMapper.toEntity(clientDto);
        String encodedPassword = passwordEncoder.encode(clientDto.getPassword());
        client.setPassword(encodedPassword);
        client.setRole(Role.USER);
        client.setActive(true);
        Client savedClient = clientRepository.save(client);
        log.info("Saved new client with email: {} and encoded password: {}", savedClient.getEmail(), encodedPassword);
        return savedClient;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Searching for user with email: {}", email);
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                client.getEmail(),
                client.getPassword(),
                client.isActive(),
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + client.getRole().name()))
        );

    }


    public ClientDto findByEmail(String email) {
        Client emailClient = clientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return ClientMapper.toDto(emailClient);
    }

    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return ClientMapper.toDto(client);
    }
}