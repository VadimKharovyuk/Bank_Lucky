package com.example.authentication.maper;

import com.example.authentication.dto.ClientDto;
import com.example.authentication.maper.CardMapper;
import com.example.authentication.model.Client;
import com.example.authentication.model.Card;
import java.util.List;
import java.util.stream.Collectors;

public class ClientMapper {

    public static ClientDto toDto(Client client) {
        if (client == null) {
            return null;
        }
        return ClientDto.builder()
                .id(client.getId())
                .username(client.getUsername())
                .email(client.getEmail())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .role(client.getRole())
                .isActive(client.isActive())
                .cards(client.getCards().stream()
                        .map(CardMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Client toEntity(ClientDto dto) {
        if (dto == null) {
            return null;
        }
        Client client = new Client();
        client.setId(dto.getId());
        client.setUsername(dto.getUsername());
        client.setEmail(dto.getEmail());
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setRole(dto.getRole());
        client.setActive(dto.isActive());
        return client;
    }
}