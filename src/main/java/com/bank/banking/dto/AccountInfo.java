package com.bank.banking.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Informasi lengkap mengenai akun pengguna")
public class AccountInfo {

    @Schema(
        description = "Nama pemilik akun",
        example = "John Doe"
    )
    private String accountName;

    @Schema(
        description = "Saldo terkini akun",
        example = "1000000.00"
    )
    private BigDecimal accountBalance;

    @Schema(
        description = "Nomor rekening yang unik",
        example = "1234567890"
    )
    private String accountNumber;

}
