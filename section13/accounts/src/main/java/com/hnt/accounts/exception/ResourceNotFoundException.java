package com.hnt.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String sourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with given data %s: %s", sourceName, fieldName, fieldValue));
    }
}
