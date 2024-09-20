package com.example.authentication.service;

import com.example.authentication.dto.CardDto;
import com.example.authentication.maper.CardMapper;
import com.example.authentication.model.Card;
import com.example.authentication.model.CardType;
import com.example.authentication.model.Client;
import com.example.authentication.repository.CardRepository;
import com.example.authentication.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ClientRepository clientRepository;


    public CardDto createCard(Long clientId, CardType cardType) {
        String cardNumber = generateCardNumber();
        LocalDateTime expirationDate = LocalDateTime.now().plusYears(5);

        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setCardType(cardType);
        card.setBalance(BigDecimal.ZERO);
        card.setExpirationDate(expirationDate);
        LocalDateTime now = LocalDateTime.now();
        card.setCreatedAt(now);
        card.setUpdatedAt(now);

        // Генерация CVV
        card.setCvv(generateCvv());

        // Получение клиента
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        card.setClient(client);

        card = cardRepository.save(card);
        return CardMapper.toDto(card);
    }

    private String generateCardNumber() {
        // Пример генерации 16-значного номера карты
        return String.valueOf(1000000000000000L + (long) (Math.random() * 9000000000000000L));
    }
    private String generateCvv() {
        Random random = new Random();
        return String.format("%03d", random.nextInt(1000)); // Генерация трехзначного числа
    }
}