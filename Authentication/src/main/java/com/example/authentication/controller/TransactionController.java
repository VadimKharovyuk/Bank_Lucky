package com.example.authentication.controller;

import com.example.authentication.dto.TransactionDto;
import com.example.authentication.exeption.InsufficientFundsException;
import com.example.authentication.service.TransferService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDto> transferMoney(
            @RequestParam(required = false) String fromCardNumber,
            @RequestParam(required = false) String toCardNumber,
            @RequestParam BigDecimal amount) {

        System.out.println(fromCardNumber + "номер карты");

        log.info("Received request with parameters - fromCardNumber: {}, toCardNumber: {}, amount: {}",
                fromCardNumber, toCardNumber, amount);
        if (fromCardNumber == null || toCardNumber == null) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            TransactionDto transactionDto = transferService.transferMoney(fromCardNumber, toCardNumber, amount);
            log.info("Transfer successful from card number: {} to card number: {}", fromCardNumber, toCardNumber);
            return ResponseEntity.ok(transactionDto);
        } catch (InsufficientFundsException e) {
            log.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Error transferring money: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}