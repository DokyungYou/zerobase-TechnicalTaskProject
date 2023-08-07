package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.request.partner.UpdateReservationDateTimeInput;
import com.zerobase.shopreservation.dto.response.ReservationResponse;
import com.zerobase.shopreservation.dto.request.partner.ResponseReservationInput;
import com.zerobase.shopreservation.dto.request.LoginInput;
import com.zerobase.shopreservation.dto.request.partner.ShopInput;
import com.zerobase.shopreservation.dto.request.partner.SignUpPartnerInput;

import java.util.List;

public interface PartnerService {
    // 파트너 회원가입
    ServiceResult signUp(SignUpPartnerInput signUpPartnerInput);

    // 파트너 로그인
    ResponseMessage login(LoginInput loginInput);

    // 상점 등록
    ServiceResult registerShop(ShopInput shopInput, String businessRegistrationNumber);

    // 상점 업데이트
    ServiceResult updateShop(Long shopId, String businessRegistrationNumber, ShopInput updateShopInput);

    // 예약목록 조회
    List<ReservationResponse> getReservation(String businessRegistrationNumber, Long shopId);

    // 예약상태 변경
    ServiceResult updateReservationStatus(Long shopId,String businessRegistrationNumber, ResponseReservationInput responseReservationInput);

    //상점 삭제
    ServiceResult deleteShop(Long shopId, String businessRegistrationNumber);

    // 파트너 회원탈퇴
    ServiceResult deletePartner(String businessRegistrationNumber);

    //예약날짜 변경
    ServiceResult updateReservationDateTime(Long shopId, String businessRegistrationNumber, UpdateReservationDateTimeInput updateReservationDateTimeInput);
}
