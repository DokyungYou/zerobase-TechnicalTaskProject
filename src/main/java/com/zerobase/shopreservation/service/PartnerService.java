package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.input.ResponseReservationInput;
import com.zerobase.shopreservation.dto.input.partner.LoginPartnerInput;
import com.zerobase.shopreservation.dto.input.partner.ShopRegisterInput;
import com.zerobase.shopreservation.dto.input.partner.SignUpPartnerInput;
import com.zerobase.shopreservation.entity.Reservation;

import java.util.List;

public interface PartnerService {
    ServiceResult signUp(SignUpPartnerInput signUpPartnerInput);

    ResponseMessage login(LoginPartnerInput loginPartnerInput);

    ServiceResult registerShop(ShopRegisterInput shopRegisterInput, String businessRegistrationNumber);

    List<Reservation> getReservation(String email, Long shopId);

    ServiceResult responseReservation(Long shopId,String email, ResponseReservationInput responseReservationInput);
}
