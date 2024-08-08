package com.hnt.accounts.functions;

import com.hnt.accounts.service.IAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AccountFunction {
    public static final Logger log = LoggerFactory.getLogger(AccountFunction.class);

    @Bean
    public Consumer<Long> updateCommunication(IAccountService iAccountService) {
        return accountNumber -> {
            log.info("Updating communication number: {}", accountNumber.toString());
            iAccountService.updateCommunicationStatus(accountNumber);
        };
    }
}
