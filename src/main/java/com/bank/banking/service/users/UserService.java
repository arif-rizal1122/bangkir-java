package com.bank.banking.service.users;

import com.bank.banking.dto.request.UserRequest;
import com.bank.banking.dto.response.BankResponse;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    


}