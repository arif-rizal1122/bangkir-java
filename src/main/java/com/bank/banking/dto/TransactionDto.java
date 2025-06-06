package com.bank.banking.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private String transactionId;
    
    private String transactionType;

    private BigDecimal amount;

    private String accountNumber;

    private String status;
}
