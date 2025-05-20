package com.bank.banking.service.bankStatment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bank.banking.dto.EmailDetails;
import com.bank.banking.entity.Transaction;
import com.bank.banking.entity.User;
import com.bank.banking.repository.TransactionRepository;
import com.bank.banking.repository.UserRepository;
import com.bank.banking.service.users.EmailService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BankStatmentImpl {
    
    @Autowired
    private TransactionRepository transactionRepository;

    private static final String FILE = "/home/arifrizal/Desktop/bankingers.pdf";

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;


    public List<Transaction> generateStatment(String accountNumber, String startDate, String endDate){

        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionList = transactionRepository.findAll().stream().filter(transaction ->
        transaction.getAccountNumber().equals(accountNumber))
        .filter(transaction -> transaction.getCreatedAt().isEqual(start))
        .filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();

        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + user.getLastName() + user.getOtherName();

        try {
            Rectangle statmentSize = new Rectangle(PageSize.A4);
                Document document = new Document(statmentSize);
                log.info("Setting size document");
                OutputStream outputStream = new FileOutputStream(FILE);
                PdfWriter.getInstance(document, outputStream);
                document.open();

                PdfPTable bankInfoTable = new PdfPTable(1);
                PdfPCell bankName = new PdfPCell(new Phrase("JAVA BANKING"));
                bankName.setBorder(0);
                bankName.setBackgroundColor(BaseColor.BLUE);
                bankName.setPadding(20f);

                PdfPCell bankAddress = new PdfPCell(new Phrase("jln. macanda pariwisata 11098 Makassar"));
                bankAddress.setBorder(0);
                bankInfoTable.addCell(bankName);
                bankInfoTable.addCell(bankAddress);

                PdfPTable statmentInfo = new PdfPTable(2);
                PdfPCell customInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
                customInfo.setBorder(0);
                PdfPCell statment = new PdfPCell(new Phrase("Statmenent Of Account"));
                statment.setBorder(0);
                PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
                stopDate.setBorder(0);

                PdfPCell name = new PdfPCell(new Phrase("Customer name: " + customerName));
                name.setBorder(0);
                PdfPCell space = new PdfPCell();
                space.setBorder(0);
                PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.getAddress()));
                address.setBorder(0);

                PdfPTable transactionsTable = new PdfPTable(4);
                PdfPCell date = new PdfPCell(new Phrase("DATE"));
                date.setBackgroundColor(BaseColor.BLUE);
                date.setBorder(0);

                PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
                transactionType.setBackgroundColor(BaseColor.BLUE);
                transactionType.setBorder(0);

                PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
                transactionAmount.setBackgroundColor(BaseColor.BLUE);
                transactionAmount.setBorder(0);
                PdfPCell status = new PdfPCell(new Phrase("STATUS"));
                status.setBackgroundColor(BaseColor.BLUE);
                status.setBorder(0);

                transactionsTable.addCell(date);
                transactionsTable.addCell(transactionType);
                transactionsTable.addCell(transactionAmount);
                transactionsTable.addCell(status);

                transactionList.forEach(transaction -> {
                    transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
                    transactionsTable.addCell(new Phrase(transaction.getTransactionType().toString()));
                    transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
                    transactionsTable.addCell(new Phrase(transaction.getStatus().toString()));
                });
                statmentInfo.addCell(customInfo);
                statmentInfo.addCell(statment);
                statmentInfo.addCell(endDate);
                statmentInfo.addCell(name);
                statmentInfo.addCell(space);
                statmentInfo.addCell(address);


                document.add(bankInfoTable);
                document.add(statmentInfo);
                document.add(transactionsTable);

                document.close();

                EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATMENT OF ACCOUNT!")
                .messageBody("kindly find your requested account statment attached!")
                .attachment(FILE)
                .build();
                emailService.sendEmailWithAttachments(emailDetails);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
     
        return transactionList;
    }


    

}
