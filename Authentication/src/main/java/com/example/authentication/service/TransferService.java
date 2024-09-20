package com.example.authentication.service;
import com.example.authentication.dto.TransactionDto;
import com.example.authentication.exeption.InsufficientFundsException;
import com.example.authentication.maper.TransactionMapper;
import com.example.authentication.model.Card;
import com.example.authentication.model.Transaction;
import com.example.authentication.model.TransactionStatus;
import com.example.authentication.repository.CardRepository;
import com.example.authentication.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;



    public TransactionDto transferMoney(String fromCardNumber, String toCardNumber, BigDecimal amount) {
        // Получение карты отправителя по номеру карты
        Card fromCard = cardRepository.findByCardNumber(fromCardNumber)
                .orElseThrow(() -> new EntityNotFoundException("Sender card not found"));

        // Проверка баланса отправителя
        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Sender does not have enough balance");
        }

        // Получение карты получателя по номеру карты
        Card toCard = cardRepository.findByCardNumber(toCardNumber)
                .orElseThrow(() -> new EntityNotFoundException("Receiver card not found"));

        // Списание средств с карты отправителя
        fromCard.setBalance(fromCard.getBalance().subtract(amount));

        // Зачисление средств на карту получателя
        toCard.setBalance(toCard.getBalance().add(amount));

        // Сохранение изменений баланса для обеих карт
        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        // Создание транзакции
        Transaction transaction = new Transaction();
        transaction.setFromCard(fromCard);
        transaction.setToCard(toCard);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.SUCCESS);

        // Сохранение транзакции
        transaction = transactionRepository.save(transaction);

        // Возврат DTO транзакции
        return TransactionMapper.toDto(transaction);
    }
}
