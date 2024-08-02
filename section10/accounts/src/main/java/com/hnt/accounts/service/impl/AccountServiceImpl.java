package com.hnt.accounts.service.impl;

import com.hnt.accounts.constans.Constants;
import com.hnt.accounts.dto.AccountDto;
import com.hnt.accounts.dto.CustomerDto;
import com.hnt.accounts.entity.Account;
import com.hnt.accounts.entity.Customer;
import com.hnt.accounts.exception.CustomerExistedException;
import com.hnt.accounts.exception.ResourceNotFoundException;
import com.hnt.accounts.mapper.AccountMapper;
import com.hnt.accounts.mapper.CustomerMapper;
import com.hnt.accounts.repository.AccountRepository;
import com.hnt.accounts.repository.CustomerRepository;
import com.hnt.accounts.service.IAccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private ModelMapper mapper;

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;

    /**
     *
     * @param customerDto
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createAccount(CustomerDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        Optional<Customer> optionalCustomer = customerRepository.findFirstByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerExistedException("Customer already existed with mobile number: " + customerDto.getMobileNumber());
        }
        Customer newCustomer = customerRepository.save(customer);
        accountRepository.save(createNewAccount(newCustomer));
    }


    /**
     *
     * @param mobileNumber
     * @return
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findFirstByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Account account = accountRepository.findFirstByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDto customerDto = mapper.map(customer, CustomerDto.class);
        customerDto.setAccountDto(mapper.map(account, AccountDto.class));
        return customerDto;
    }

    /**
     *
     * @param customerDto
     * @return
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountDto accountDto = customerDto.getAccountDto();
        if(accountDto !=null ){
            Account account = accountRepository.findById(accountDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountDto.getAccountNumber().toString())
            );
            AccountMapper.mapToAccount(accountDto, account);
            account = accountRepository.save(account);

            Long customerId = account.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     *
     * @param mobileNumber
     * @return
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findFirstByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    /**
     * @param customer
     * @return account
     */
    private Account createNewAccount(Customer customer) {
        Account newAccount = new Account();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(Constants.SAVINGS);
        newAccount.setBranchAddress(Constants.ADDRESS);
        return newAccount;
    }


}
