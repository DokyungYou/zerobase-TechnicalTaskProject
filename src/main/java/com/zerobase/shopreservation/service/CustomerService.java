package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.input.LoginCustomerInput;
import com.zerobase.shopreservation.dto.input.ReservationShopInput;
import com.zerobase.shopreservation.dto.input.ReviewInput;
import com.zerobase.shopreservation.dto.input.SignUpCustomerInput;

public interface CustomerService {
    ServiceResult signUp(SignUpCustomerInput signUpCustomerInput);

    ResponseMessage login(LoginCustomerInput loginCustomerInput);


    ServiceResult reservationShop(String email, ReservationShopInput reservationShopInput);

    ServiceResult reviewShop(Long reservedId,String email, ReviewInput reviewInput);
}
