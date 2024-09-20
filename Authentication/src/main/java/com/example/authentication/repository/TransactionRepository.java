package com.example.authentication.repository;

import com.example.authentication.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository  extends JpaRepository<Transaction,Long> {
}
