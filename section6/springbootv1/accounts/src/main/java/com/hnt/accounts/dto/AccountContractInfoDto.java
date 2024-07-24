package com.hnt.accounts.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "accounts")
public record AccountContractInfoDto(String message, Map<String, String> contactDetails, List<String> onCallSupport) {
}
