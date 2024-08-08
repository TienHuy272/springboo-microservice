package com.hnt.accounts.dto;

/**
 *
 * @param accountNumber
 * @param name
 * @param email
 * @param mobileNumber
 */
public record AccountMsgDto(Long accountNumber,
                            String name,
                            String email,
                            String mobileNumber) {

}
