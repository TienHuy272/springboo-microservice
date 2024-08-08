package com.hnt.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomerExistedException extends RuntimeException{
    public CustomerExistedException(String message) {
        super(message);
    }
}
