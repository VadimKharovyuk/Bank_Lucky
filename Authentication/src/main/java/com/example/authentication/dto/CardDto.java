package com.example.authentication.dto;

import com.example.authentication.model.CardType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class CardDto {
    private Long id;


     private Long clientId;
    private String cardNumber;
    private LocalDateTime expirationDate;
    private CardType cardType;
    private BigDecimal balance;
}
