package com.bank.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bank.banking.dto.request.CreditDebitRequest;
import com.bank.banking.dto.request.EnquiryRequest;
import com.bank.banking.dto.request.TransferRequest;
import com.bank.banking.dto.request.UserRequest;
import com.bank.banking.dto.response.BankResponse;
import com.bank.banking.service.users.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs", description = "Endpoints for managing user accounts, balance enquiries, and transactions")
public class UserController {
    
    @Autowired 
    UserService userService;

    @PostMapping("/create")
    @Operation(
        summary = "Create New User Account",
        description = "Endpoint to create a new user and assign a unique account ID"
    )
    @ApiResponse(responseCode = "201", description = "User account created successfully")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

    @GetMapping("/balanceEnquiry")
    @Operation(
        summary = "Balance Enquiry",
        description = "Retrieve the current balance for a specific account"
    )
    @ApiResponse(responseCode = "200", description = "Balance retrieved successfully")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
        return userService.balanceEnquiry(request);
    }

    @GetMapping("/nameEnquiry")
    @Operation(
        summary = "Name Enquiry",
        description = "Retrieve the account holder's name for a specific account number"
    )
    @ApiResponse(responseCode = "200", description = "Account name retrieved successfully")
    public String nameEnquiry(@RequestBody EnquiryRequest request){
        return userService.nameEnquiry(request);
    }

    @PostMapping("/credit")
    @Operation(
        summary = "Credit Account",
        description = "Credit a specified amount to an account"
    )
    @ApiResponse(responseCode = "200", description = "Amount credited successfully")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest request){
        return userService.creditAccount(request);
    }

    @PostMapping("/debit")
    @Operation(
        summary = "Debit Account",
        description = "Debit a specified amount from an account"
    )
    @ApiResponse(responseCode = "200", description = "Amount debited successfully")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest request){
        return userService.debitAccount(request);
    }

    @PostMapping("/transfer")
    @Operation(
        summary = "Transfer Funds",
        description = "Transfer funds from one account to another"
    )
    @ApiResponse(responseCode = "200", description = "Transfer completed successfully")
    public BankResponse transfer(@RequestBody TransferRequest request){
        return userService.transfer(request);
    }

}
