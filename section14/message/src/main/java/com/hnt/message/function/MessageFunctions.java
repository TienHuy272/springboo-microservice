package com.hnt.message.function;

import com.hnt.message.dto.AccountMsgDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;


@Configuration
public class MessageFunctions {
    public static final Logger log = LoggerFactory.getLogger(MessageFunctions.class);

    @Bean
    public Function<AccountMsgDto, AccountMsgDto> email() {  // <Input, Output>
        return accountMsgDto -> {
            log.info("STEP2: Sending email with details: {}", accountMsgDto.toString());
            return accountMsgDto;
        };
    }

    @Bean
    public Function<AccountMsgDto, Long> sms() {  // <Input, Output>
        return accountMsgDto -> {
            log.info("STEP3: Sending sms with details: {}", accountMsgDto.toString());
            return accountMsgDto.accountNumber();
        };
    }

}
