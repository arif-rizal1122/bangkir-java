package com.bank.banking.service.transaction;

import com.bank.banking.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto transaction);
}
