package com.example.authentication.dto;

import com.example.authentication.model.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;

    private String fromCardId;
    private String toCardId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private TransactionStatus status;
}
