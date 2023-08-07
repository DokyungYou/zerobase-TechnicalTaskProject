package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.response.ReviewResponse;
import com.zerobase.shopreservation.dto.request.LoginInput;
import com.zerobase.shopreservation.dto.request.customer.*;
import com.zerobase.shopreservation.entity.Reservation;

import java.util.List;

public interface CustomerService {
    
    // 회원가입
    ServiceResult signUp(SignUpCustomerInput signUpCustomerInput);

    // 로그인
    ResponseMessage login(LoginInput loginInput);

    // 예약
    ServiceResult reservationShop(String email, ReservationShopInput reservationShopInput);

    // 리뷰남기기
    ServiceResult reviewShop(Long reservedId,String email, ReviewInput reviewInput);

    // 도착체크(키오스크)
    ServiceResult arriveCheck(long shopId, ArriveCheckInput arriveCheckInput);

    // 내 예약목록
    List<Reservation> getMyReservationList(String email);

    // 내 리뷰목록
    List<ReviewResponse> getMyReviewList(String email);


    // 회원탈퇴
    ServiceResult unregisterCustomer(String email);

    // 예약취소
    ServiceResult cancelReservation(Long reservationId, String email);

}
