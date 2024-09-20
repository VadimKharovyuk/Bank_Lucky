package com.example.authentication.repository;

import com.example.authentication.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,Long> {
    Optional<Card> findByCardNumber(String cardNumber);
}
