package com.bank.banking.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.banking.dto.AccountInfo;
import com.bank.banking.dto.EmailDetails;
import com.bank.banking.dto.request.CreditDebitRequest;
import com.bank.banking.dto.request.EnquiryRequest;
import com.bank.banking.dto.request.UserRequest;
import com.bank.banking.dto.response.BankResponse;
import com.bank.banking.entity.User;
import com.bank.banking.repository.UserRepository;
import com.bank.banking.utils.AccountUtils;
import com.bank.banking.utils.UserMapper;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    EmailServiceImpl emailService;
    
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * 1. creating an account - saving a new user into the db
         * 2. check if user already has an account
         * 
         * */ 
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            BankResponse response = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
                    return response;
        }
        User newUser = userMapper.toUser(userRequest);

        User savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder()
        .recipient(savedUser.getEmail())
        .subject("ACCOUNT CREATED")
        .messageBody("Congratulations! Your Account has been successfully created. \nYour Account Details : \n"
         + " Account Name " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + " " + " Account Number\n" + savedUser.getAccountNumber())
        .build();
        emailService.sendEmailAlert(emailDetails);

            return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
            .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
            .accountInfo(AccountInfo.builder()
            .accountBalance(savedUser.getAccountBalance())
            .accountNumber(savedUser.getAccountNumber())
            .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
            .build()) 
        .build();
    }






    /**
     * balance enquery, name enquery, credit, debit, transfer
     * 
     * */ 
    

     @Override
     public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        // check if the provided account number existsin the exists
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExists) {
            return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_NUMBER_EXISTS_CODE)
            .responseMessage(AccountUtils.ACCOUNT_NUMBER__EXISTS_MESSAGE)
            .accountInfo(null)
            .build();
        }
        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
               .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
               .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
               .accountInfo(AccountInfo.builder()
               .accountBalance(foundUser.getAccountBalance())
               .accountNumber(enquiryRequest.getAccountNumber())
               .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName())
               .build())
               .build();
     }
 
     @Override
     public String nameEnquiry(EnquiryRequest request) {
         boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
         if (!isAccountExists) {
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
         }
         User foundUser = userRepository.findByAccountNumber(request.getAccountNumber()); 
         return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
     }



     @Override
     public BankResponse creditAccount(CreditDebitRequest request) {
         boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
         if (!isAccountExists) {
            return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_NUMBER_EXISTS_CODE)
            .responseMessage(AccountUtils.ACCOUNT_NUMBER__EXISTS_MESSAGE)
            .accountInfo(null)
            .build();
        }
        User userCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userCredit.setAccountBalance(userCredit.getAccountBalance().add(request.getAmount()));

        return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
            .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
            .accountInfo(AccountInfo.builder()
            .accountNumber(request.getAccountNumber())
            .accountName(userCredit.getFirstName() + " " + userCredit.getLastName() + " " + userCredit.getOtherName())
            .accountBalance(userCredit.getAccountBalance())
            .build())
            .build();
     }

 
 
    
}
