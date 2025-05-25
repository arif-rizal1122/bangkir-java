package com.bank.banking.utils;

import java.math.BigDecimal;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bank.banking.dto.request.UserRequest;
import com.bank.banking.entity.User;
import com.bank.banking.entity.Role;

@Mapper(componentModel = "spring", imports = {AccountUtils.class, BigDecimal.class, Role.class})
public interface UserMapper {
    @Mapping(target = "accountNumber", expression = "java(AccountUtils.generatedAccountNumber())")
    @Mapping(target = "accountBalance", expression = "java(BigDecimal.ZERO)")
    @Mapping(target = "password",  expression = "java(passwordEncoder.encode(request.getPassword()))")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "role", expression = "java(Role.ROLE_USER)") 
    @Mapping(target = "id", ignore = true) 
    User toUser(UserRequest request, @Context PasswordEncoder passwordEncoder);
}

