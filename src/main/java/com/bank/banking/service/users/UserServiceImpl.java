package com.bank.banking.service.users;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.banking.config.JwtTokenProvider;
import com.bank.banking.dto.AccountInfo;
import com.bank.banking.dto.EmailDetails;
import com.bank.banking.dto.TransactionDto;
import com.bank.banking.dto.request.CreditDebitRequest;
import com.bank.banking.dto.request.EnquiryRequest;
import com.bank.banking.dto.request.LoginDtoRequest;
import com.bank.banking.dto.request.TransferRequest;
import com.bank.banking.dto.request.UserRequest;
import com.bank.banking.dto.response.BankResponse;
import com.bank.banking.entity.User;
import com.bank.banking.repository.UserRepository;
import com.bank.banking.service.transaction.TransactionService;
import com.bank.banking.utils.AccountUtils;
import com.bank.banking.utils.UserMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    @Autowired
    TransactionService transactionService;


    @Autowired
    private UserMapper userMapper;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;
    
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        // 1. Check if user already has an account
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
    
        // 2. Convert request to user using mapper, pass in passwordEncoder as context
        User user = userMapper.toUser(userRequest, passwordEncoder);
    
        // 3. Save user
        User savedUser = userRepository.save(user);
    
        // 4. Send email
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATED")
                .messageBody("Congratulations! Your Account has been successfully created.\n"
                        + "Your Account Details:\n"
                        + "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "\n"
                        + "Account Number: " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);
    
        // 5. Return response
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
        userRepository.save(userCredit);

        // save transaction
        TransactionDto transactionDto = TransactionDto.builder()
        .accountNumber(userCredit.getAccountNumber())
        .transactionType("CREDIT")
        .amount(request.getAmount())
        .build();

        transactionService.saveTransaction(transactionDto);

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

 
 
     @Override
     public BankResponse debitAccount(CreditDebitRequest request) {
        // check if the account exists
        boolean isAccountExists = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExists) {
           return BankResponse.builder()
           .responseCode(AccountUtils.ACCOUNT_NUMBER_EXISTS_CODE)
           .responseMessage(AccountUtils.ACCOUNT_NUMBER__EXISTS_MESSAGE)
           .accountInfo(null)
           .build();
       }
        // check if the amount you intead to withdraw is not more than

       User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
       BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
       BigInteger debitAmount = request.getAmount().toBigInteger();
 
       if (availableBalance.intValue() <= debitAmount.intValue()) {
           return BankResponse.builder()
               .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
               .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
               .accountInfo(null)
           .build();

       } else {

            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            // save transaction
            TransactionDto transactionDto = TransactionDto.builder()
            .accountNumber(userToDebit.getAccountNumber())
            .transactionType("DEBIT")
            .amount(request.getAmount())
            .build();
            transactionService.saveTransaction(transactionDto);

            return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                .accountNumber(request.getAccountNumber())
                .accountBalance(userToDebit.getAccountBalance())
                .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                .build())
            .build();
       }

     }






     @Override
     public BankResponse transfer(TransferRequest request) {
        // get account to debit (check if it exists)
        // check if the amount in debiting is not more than current balance
        // debit the account
        // get the account to credit 
        // credit the account
        boolean isDestinationAccountExists = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isDestinationAccountExists) {
            return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_NUMBER_EXISTS_CODE)
            .responseMessage(AccountUtils.ACCOUNT_NUMBER__EXISTS_MESSAGE)
            .accountInfo(null)
            .build();
        }

        User sourceUserAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if (request.getAmount().compareTo(sourceUserAccountUser.getAccountBalance()) > 0) {
            return BankResponse.builder()
            .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
            .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
            .accountInfo(null)
        .build();
        }
        
        sourceUserAccountUser.setAccountBalance(sourceUserAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceUserAccountUser);
        
        EmailDetails debitAlerts = EmailDetails.builder()
        .subject("DEBIT ALERT!!")
        .recipient(sourceUserAccountUser.getEmail())
        .messageBody("the sum of " + " " + request.getAmount() + " has been deducted from your account!, your current balance is :" + sourceUserAccountUser.getAccountBalance())
        .build();
        emailService.sendEmailAlert(debitAlerts);


        
        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);

        EmailDetails creditsAlerts = EmailDetails.builder()
        .subject("CREDIT ALERT!!")
        .recipient(sourceUserAccountUser.getEmail())
        .messageBody("the sum of " + " " + request.getAmount() + " has been sent to your account from :" + sourceUserAccountUser.getAccountBalance())
        .build();
        emailService.sendEmailAlert(creditsAlerts);

        // save transaction
        TransactionDto transactionDto = TransactionDto.builder()
        .accountNumber(destinationAccountUser.getAccountNumber())
        .transactionType("CREDIT")
        .amount(request.getAmount())
        .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
        .responseCode(AccountUtils.TRANSFER_SUCCESSFULL_CODE)
        .responseMessage(AccountUtils.TRANSFER_SUUCCESSFULL_MESSAGE)
        .accountInfo(null)
        .build();
     }



     @Override
     public BankResponse login(LoginDtoRequest loginDto) {

        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        
        EmailDetails loginAlert = EmailDetails.builder()
        .subject("You're logged in!")
        .recipient(loginDto.getEmail())
        .messageBody("you logged into your account. if you did initiate this request please contact your bank")
        .build();
        emailService.sendEmailAlert(loginAlert);

        return BankResponse.builder()
        .responseCode("login success")
        .responseMessage(jwtTokenProvider.generateToken(authentication))
        .build();


    }



 
}
