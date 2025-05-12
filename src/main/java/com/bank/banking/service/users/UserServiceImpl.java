package com.bank.banking.service.users;

import com.bank.banking.dto.request.UserRequest;
import com.bank.banking.dto.response.BankResponse;

public interface UserServiceImpl {

    BankResponse createAccount(UserRequest userRequest);
    


}