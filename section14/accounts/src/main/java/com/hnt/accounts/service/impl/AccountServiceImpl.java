package com.hnt.accounts.service.impl;

import com.hnt.accounts.constans.Constants;
import com.hnt.accounts.dto.AccountDto;
import com.hnt.accounts.dto.AccountMsgDto;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    public static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private ModelMapper mapper;

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private final StreamBridge streamBridge;

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
        Account savedAccount = accountRepository.save(createNewAccount(newCustomer));
        sendCommunication(savedAccount, newCustomer);
    }

    private void sendCommunication(Account account, Customer customer) {
        var accountsMsgDto = new AccountMsgDto(account.getAccountNumber(), customer.getName(),
                customer.getEmail(), customer.getMobileNumber());
        log.info("STEP1: Sending Communication request for the details: {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        log.info("Is the Communication request successfully triggered ? : {}", result);
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

    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if(accountNumber !=null ){
            Account accounts = accountRepository.findById(accountNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
            );
            accounts.setCommunicationSw(true);
            accountRepository.save(accounts);
            isUpdated = true;
        }
        return  isUpdated;
    }
}
