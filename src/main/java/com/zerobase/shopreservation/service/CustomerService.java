package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.input.SignUpCustomerInput;

public interface CustomerService {
    ServiceResult signUp(SignUpCustomerInput signUpCustomerInput);
}
