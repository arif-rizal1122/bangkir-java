package com.bank.banking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.banking.entity.Transaction;
import com.bank.banking.service.bankStatment.BankStatmentImpl;


@RestController
@RequestMapping("/api/bank")
public class StatmentController {
    @Autowired
    private BankStatmentImpl bankStatment;

    @PostMapping("/statment")
    public List<Transaction> generateTransactions(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate){
        List<Transaction> transactionList = bankStatment.generateStatment(accountNumber, startDate, endDate);
        return transactionList;
    }

}
