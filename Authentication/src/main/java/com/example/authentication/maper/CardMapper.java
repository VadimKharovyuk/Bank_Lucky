package com.example.authentication.maper;

import com.example.authentication.dto.CardDto;
import com.example.authentication.model.Card;

public class CardMapper {

    public static CardDto toDto(Card card) {
        if (card == null) {
            return null;
        }
        return CardDto.builder()
                .id(card.getId())
                .cardNumber(card.getCardNumber())
                .cardType(card.getCardType())
                .balance(card.getBalance())
                .clientId(card.getClient().getId())

                .expirationDate(card.getExpirationDate())
                .build();
    }

    public static Card toEntity(CardDto dto) {
        if (dto == null) {
            return null;
        }
        Card card = new Card();
        card.setId(dto.getId());
        card.setCardNumber(dto.getCardNumber());
        card.setCardType(dto.getCardType());
        card.setBalance(dto.getBalance());
        card.setExpirationDate(dto.getExpirationDate());
        return card;
    }
}