package com.hnt.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "Customer",description = "Schema to hold Customer and Account information")
public class CustomerDto {

    @Schema(name = "Name of customer", example = "Huy Nguyen Tien")
    @NotEmpty(message = "Name can not be empty")
    @Size(min = 5, max = 30, message = "Length of customer name should be between 5 and 30")
    private String name;

    @Schema(name = "Email address of customer", example = "luvspring@gmail.com")
    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Email address is invalid")
    private String email;

    @Schema(name = "Phone number of customer", example = "0123456789")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(description = "Account detail of customer")
    private AccountDto accountDto;
}
