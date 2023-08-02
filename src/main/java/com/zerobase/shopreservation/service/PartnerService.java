package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.input.partner.ResponseReservationInput;
import com.zerobase.shopreservation.dto.input.partner.LoginPartnerInput;
import com.zerobase.shopreservation.dto.input.partner.ShopInput;
import com.zerobase.shopreservation.dto.input.partner.SignUpPartnerInput;
import com.zerobase.shopreservation.entity.Reservation;

import java.util.List;

public interface PartnerService {
    ServiceResult signUp(SignUpPartnerInput signUpPartnerInput);

    ResponseMessage login(LoginPartnerInput loginPartnerInput);

    ServiceResult registerShop(ShopInput shopInput, String businessRegistrationNumber);

    ServiceResult updateShop(Long shopId, String email, ShopInput updateShopInput);

    List<Reservation> getReservation(String email, Long shopId);

    ServiceResult responseReservation(Long shopId,String email, ResponseReservationInput responseReservationInput);

}
