package com.hnt.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {
    @NotEmpty(message = "Name can not be empty")
    @Size(min = 5, max = 30, message = "Length of customer name should be between 5 and 30")
    private String name;

    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Email address is invalid")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;
    private AccountDto accountDto;
}
