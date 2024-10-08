package com.hnt.accounts.service.client;

import com.hnt.accounts.dto.LoanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("loans")
public interface ILoansFeignClient {

    @GetMapping(value = "/api/v1/fetch", consumes = "application/json")
    ResponseEntity<LoanDto> fetchLoanDetails(@RequestHeader("hnt-correlation-id") String correlationId,
                                             @RequestParam String mobileNumber);

}
