package com.hnt.accounts.service.client;

import com.hnt.accounts.dto.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cards")
public interface ICardsFeignClient {

    @GetMapping(value = "/api/cards/v1/fetch", consumes = "application/json")
    ResponseEntity<CardDto> fetchCardDetails(@RequestParam String mobileNumber);

}
