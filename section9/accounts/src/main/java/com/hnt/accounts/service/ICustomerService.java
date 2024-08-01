package com.hnt.accounts.service;

import com.hnt.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {
    /**
      * @param mobileNumber
     * @return
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
}
