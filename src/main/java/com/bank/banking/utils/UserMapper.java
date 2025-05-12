package com.bank.banking.utils;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.bank.banking.dto.request.UserRequest;
import com.bank.banking.entity.User;


@Mapper(componentModel = "spring", imports = {AccountUtils.class, BigDecimal.class})
public interface UserMapper {
    @Mapping(target = "accountNumber", expression = "java(AccountUtils.generatedAccountNumber())")
    @Mapping(target = "accountBalance", expression = "java(BigDecimal.ZERO)")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "id", ignore = true) 
    User toUser(UserRequest request);
}

