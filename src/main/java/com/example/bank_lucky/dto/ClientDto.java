package com.example.bank_lucky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ClientDto {

    private String email;
    private String password; // Добавлено поле для пароля


    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Role role;
    private boolean isActive;
    private List<CardDto> cards; // Список карт клиента




}