package com.bank.banking.utils;

import java.time.Year;

public class AccountUtils  {

  
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "this user already has an account created!!";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "account has been successfully created";


    public static final String ACCOUNT_NUMBER_EXISTS_CODE = "001";
    public static final String ACCOUNT_NUMBER__EXISTS_MESSAGE = "user with the provided account number does not exists!!";
    public static final String ACCOUNT_FOUND_CODE = "002";
    public static final String ACCOUNT_FOUND_SUCCESS = "account has been found";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "account not found";

    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_MESSAGE = "user credited successfully!!";

    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Account has been successfully debited!!";


    public static final String TRANSFER_SUCCESSFULL_CODE= "008";
    public static final String TRANSFER_SUUCCESSFULL_MESSAGE= "transfer successfull";


    public static String generatedAccountNumber(){
    /**
     * 2025 + random 6 digits
     * 
     * 
     * 
     * */ 

     Year currentYear = Year.now();
     int min = 100000;
     int max = 999999;

     // generate a random number between min and max
     int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

     String year = String.valueOf(currentYear);
     String randomNumber = String.valueOf(randNumber);

     StringBuilder accountNumber = new StringBuilder();

       return accountNumber.append(year).append(randomNumber).toString();
     }

}
