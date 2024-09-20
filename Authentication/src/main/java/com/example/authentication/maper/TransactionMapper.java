package com.example.authentication.maper;

import com.example.authentication.dto.TransactionDto;
import com.example.authentication.model.Card;
import com.example.authentication.model.Transaction;

public class TransactionMapper {

    public static TransactionDto toDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return TransactionDto.builder()
                .id(transaction.getId())
                .fromCardId(transaction.getFromCard().getCardNumber()) // Получаем номер карты
                .toCardId(transaction.getToCard().getCardNumber()) // Получаем номер карты
                .amount(transaction.getAmount())
                .timestamp(transaction.getTimestamp())
                .status(transaction.getStatus())
                .build();
    }

    public static Transaction toEntity(TransactionDto transactionDto, Card fromCard, Card toCard) {
        if (transactionDto == null || fromCard == null || toCard == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setId(transactionDto.getId());
        transaction.setFromCard(fromCard);
        transaction.setToCard(toCard);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTimestamp(transactionDto.getTimestamp());
        transaction.setStatus(transactionDto.getStatus());
        return transaction;
    }
}