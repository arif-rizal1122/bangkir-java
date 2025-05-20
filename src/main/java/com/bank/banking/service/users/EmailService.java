package com.bank.banking.service.users;

import com.bank.banking.dto.EmailDetails;

public interface EmailService {
    
    void sendEmailAlert(EmailDetails emailDetails);
    void sendEmailWithAttachments(EmailDetails emailDetails);
}
