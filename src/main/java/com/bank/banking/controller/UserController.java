package com.bank.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.banking.dto.request.UserRequest;
import com.bank.banking.dto.response.BankResponse;
import com.bank.banking.service.users.UserServiceImpl;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired 
    UserServiceImpl userService;

    @PostMapping("/create")
    public  BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createAccount(userRequest);
    }

}
