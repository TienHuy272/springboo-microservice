package com.hnt.accounts.service.impl;

import com.hnt.accounts.dto.AccountDto;
import com.hnt.accounts.dto.CardDto;
import com.hnt.accounts.dto.CustomerDetailsDto;
import com.hnt.accounts.dto.LoanDto;
import com.hnt.accounts.entity.Account;
import com.hnt.accounts.entity.Customer;
import com.hnt.accounts.exception.ResourceNotFoundException;
import com.hnt.accounts.mapper.AccountMapper;
import com.hnt.accounts.mapper.CustomerMapper;
import com.hnt.accounts.repository.AccountRepository;
import com.hnt.accounts.repository.CustomerRepository;
import com.hnt.accounts.service.ICustomerService;
import com.hnt.accounts.service.client.ICardsFeignClient;
import com.hnt.accounts.service.client.ILoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private ICardsFeignClient iCardsFeignClient;
    private ILoansFeignClient iLoansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findFirstByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Account account = accountRepository.findFirstByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountDto(AccountMapper.mapToAccountDto(account, new AccountDto()));

        ResponseEntity<LoanDto> loanDtoResponseEntity = iLoansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        customerDetailsDto.setLoanDto(loanDtoResponseEntity.getBody());

        ResponseEntity<CardDto> cardDtoResponseEntity = iCardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        customerDetailsDto.setCardDto(cardDtoResponseEntity.getBody());
        return customerDetailsDto;
    }

}
