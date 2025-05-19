package com.bank.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Banking API",
        description = "API untuk layanan perbankan digital, termasuk manajemen akun, transaksi, dan autentikasi pengguna.",
        version = "1.0.0",
        contact = @Contact(
            name = "IT Support - Digital Banking",
            email = "support@bankexample.com",
            url = "https://www.bankexample.com/support"
        ),
        license = @License(
            name = "Proprietary License - BankExample",
            url = "https://www.bankexample.com/license"
        )
    ),
    externalDocs = @ExternalDocumentation(
        description = "Dokumentasi Teknis Lengkap",
        url = "https://docs.bankexample.com/api"
    )
)

public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

}
