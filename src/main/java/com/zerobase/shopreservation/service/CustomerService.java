package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.GetShopList;
import com.zerobase.shopreservation.dto.input.*;
import com.zerobase.shopreservation.entity.Shop;

import java.util.List;

public interface CustomerService {
    ServiceResult signUp(SignUpCustomerInput signUpCustomerInput);

    ResponseMessage login(LoginCustomerInput loginCustomerInput);


    ServiceResult reservationShop(String email, ReservationShopInput reservationShopInput);

    ServiceResult reviewShop(Long reservedId,String email, ReviewInput reviewInput);

    List<GetShopList> getShopList(GetShopListInput getShopListInput);

    ServiceResult arriveCheck(long shopId,ArriveCheckInput arriveCheckInput);
}
