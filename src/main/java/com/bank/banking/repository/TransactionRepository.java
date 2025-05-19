package com.bank.banking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bank.banking.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
}
