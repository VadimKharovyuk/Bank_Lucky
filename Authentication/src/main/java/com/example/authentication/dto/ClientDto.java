package com.example.authentication.dto;

import com.example.authentication.model.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
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
