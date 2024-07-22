package com.hnt.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "Accounts", description = "Schema to hold Account information")
public class AccountDto {

    @Schema(description = "Account Number of Eazy Bank account", example = "8989889898")
    @NotEmpty(message = "AccountNumber can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{10})",message = "AccountNumber must be 10 digits")
    private Long accountNumber;

    @Schema(description = "Account type of VCB Bank account", example = "Savings")
    @NotEmpty(message = "AccountType can not be a null or empty")
    private String accountType;

    @Schema(description = "VCB Bank branch address", example = "123 Hanoi")
    @NotEmpty(message = "BranchAddress can not be a null or empty")
    private String branchAddress;

}
