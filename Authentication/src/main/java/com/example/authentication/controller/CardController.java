package com.example.authentication.controller;

import com.example.authentication.dto.CardDto;
import com.example.authentication.model.CardType;
import com.example.authentication.service.CardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Slf4j
public class CardController {

    private final CardService cardService;

    @PostMapping("/{clientId}")
    public ResponseEntity<CardDto> createCard(
            @PathVariable Long clientId,
            @RequestParam CardType cardType) {
        log.info("Received request to create card for client ID: {} with card type: {}", clientId, cardType);
        try {
            CardDto cardDto = cardService.createCard(clientId, cardType);
            log.info("Successfully created card for client ID: {}", clientId);
            return ResponseEntity.status(HttpStatus.CREATED).body(cardDto);
        } catch (EntityNotFoundException e) {
            log.error("Client not found: {}", clientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error creating card for client ID: {}", clientId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}