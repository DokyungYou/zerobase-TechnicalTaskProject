package com.zerobase.shopreservation.controller;

import com.zerobase.shopreservation.common.ResponseError;
import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ResponseResult;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.request.LoginInput;
import com.zerobase.shopreservation.dto.request.customer.ArriveCheckInput;
import com.zerobase.shopreservation.dto.request.customer.ReservationShopInput;
import com.zerobase.shopreservation.dto.request.customer.ReviewInput;
import com.zerobase.shopreservation.dto.request.customer.SignUpCustomerInput;
import com.zerobase.shopreservation.dto.response.ReviewResponse;
import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.service.CustomerService;
import com.zerobase.shopreservation.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    // 이용자 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signUpCustomer(
            @RequestBody @Valid SignUpCustomerInput signUpCustomerInput,
            Errors errors){

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

        ServiceResult result = customerService.signUp(signUpCustomerInput);
        return ResponseResult.result(result);
    }


    // 이용자 로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody @Valid LoginInput loginCustomerInput,
                    Errors errors){

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

        ResponseMessage result = customerService.login(loginCustomerInput);
        return ResponseEntity.ok().body(result);
    }


    
    
    
    //예약하기
    @PostMapping("/reservation")
    public ResponseEntity<?> reservationShop(
                    @RequestHeader("C-TOKEN") String token,
                    @RequestBody @Valid ReservationShopInput reservationShopInput,
                                             Errors errors){

        String email = JwtUtils.getIssuer(token);

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }


        ServiceResult result = customerService.reservationShop(email,reservationShopInput);

        return ResponseResult.result(result);
    }


    // 본인이 예약한 목록 가져오기
    @GetMapping("/reservation")
    public ResponseEntity<?> getMyReservationList(@RequestHeader("C-TOKEN") String token){

        String email = JwtUtils.getIssuer(token);
        List<Reservation> reservationList = customerService.getMyReservationList(email);

        return ResponseResult.success(reservationList);
    }
    
    
    // 예약취소 (예약날짜 7일 전까지만 가능)
    @PatchMapping ("/reservation/{reservationId}/cancellation")
    public ResponseEntity<?> cancelReservation(
                                    @PathVariable Long reservationId,
                                    @RequestHeader("C-TOKEN") String token){

        String email = JwtUtils.getIssuer(token);
        ServiceResult result = customerService.cancelReservation(reservationId,email);

        return ResponseResult.success(result);
    }



    // 키오스크 도착확인 (키오스크에서는 편의상 휴대전화번호로 접근하게끔 하였음)
    @PatchMapping("/kiosk/{shopId}")
    public ResponseEntity<?> arriveCheck(@PathVariable long shopId,
                                         @RequestBody ArriveCheckInput arriveCheckInput,
                                         Errors errors){

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

        ServiceResult result = customerService.arriveCheck(shopId,arriveCheckInput);
        return ResponseResult.result(result);
    }



    //리뷰 작성
    @PostMapping("/review/{reservedId}")
    public ResponseEntity<?> review(
                                    @PathVariable Long reservedId,
                                    @RequestHeader("C-TOKEN") String token,
                                    @RequestBody ReviewInput reviewInput,
                                    Errors errors
                                    ){

        String email = JwtUtils.getIssuer(token);

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

        ServiceResult result = customerService.reviewShop(reservedId,email,reviewInput);

        return ResponseResult.result(result);
    }



    // 본인이 작성했던 리뷰 목록 가져오기
    @GetMapping("/reviewed")
    public ResponseEntity<?> getMyReviewList(@RequestHeader("C-TOKEN") String token){

        String email = JwtUtils.getIssuer(token);
        List<ReviewResponse> myReviewListResponse = customerService.getMyReviewList(email);

        return ResponseResult.success(myReviewListResponse);
    }


    // 회원탈퇴 (탈퇴 시 해당 계정의 예약정보는 삭제, 남겼던 리뷰는 남게끔 하였음)
    @DeleteMapping("/unregister")
    public ResponseEntity<?> unregisterCustomer(@RequestHeader("C-TOKEN") String token){

        String email = JwtUtils.getIssuer(token);
        ServiceResult result = customerService.unregisterCustomer(email);
        return ResponseResult.success(result);
    }

}
