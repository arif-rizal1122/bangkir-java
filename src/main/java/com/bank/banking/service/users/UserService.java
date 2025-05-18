package com.bank.banking.service.users;

import com.bank.banking.dto.request.CreditDebitRequest;
import com.bank.banking.dto.request.EnquiryRequest;
import com.bank.banking.dto.request.TransferRequest;
import com.bank.banking.dto.request.UserRequest;
import com.bank.banking.dto.response.BankResponse;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest request);

    BankResponse creditAccount(CreditDebitRequest request);

    BankResponse debitAccount(CreditDebitRequest request);
    
    BankResponse transfer(TransferRequest request);
}