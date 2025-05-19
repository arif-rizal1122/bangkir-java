package com.bank.banking.service.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.banking.dto.TransactionDto;
import com.bank.banking.entity.Transaction;
import com.bank.banking.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService{

    @Autowired
    TransactionRepository transactionRepository;


    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction =  Transaction.builder()
        .transactionType(transactionDto.getTransactionType())
        .accountNumber(transactionDto.getAccountNumber())
        .amount(transactionDto.getAmount())
        .status("SUCCESS")
        .build();   
    
        transactionRepository.save(transaction);
        System.out.println("transaction saved successfully");
    }
    
    
}
