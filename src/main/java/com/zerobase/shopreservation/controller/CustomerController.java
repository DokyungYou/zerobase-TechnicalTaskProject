package com.zerobase.shopreservation.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.zerobase.shopreservation.ResponseResult;
import com.zerobase.shopreservation.common.ResponseError;
import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.GetShopList;
import com.zerobase.shopreservation.dto.input.*;
import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.entity.Review;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.service.CustomerService;
import com.zerobase.shopreservation.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // 이용자 회원가입
    @PostMapping("/api/customer/signup")
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
    @PostMapping("/api/customer/login")
    public ResponseEntity<?> loginCustomer(@RequestBody @Valid LoginCustomerInput loginCustomerInput,
                    Errors errors){

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

        ResponseMessage result = customerService.login(loginCustomerInput);
        return ResponseEntity.ok().body(result);
    }


    // 상점 목록 가져오기 (선택사항, 정렬방식 등)
    // 상점 목록 조회하는 것도 꼭 로그인(토큰)해야만 하는걸로 해야하나..?
    @GetMapping("api/customer/shops")
    public ResponseEntity<?> getShopList(@RequestBody GetShopListInput getShopListInput){


        List<GetShopList> shopList = customerService.getShopList(getShopListInput);

        return ResponseResult.success(shopList);
    }


    // 특정상점 detail 정보 가져오기
    @GetMapping("api/customer/{shopId}")
    public ResponseEntity<?> getShopDetail(@PathVariable Long shopId){

        Shop shop = customerService.getShopDetail(shopId);
        return ResponseResult.success(shop);
    }
    
    
    // 특정 상점 리뷰목록 가져오기
    @GetMapping("api/customer/{shopId}/review")
    public ResponseEntity<?> getShopReviews(@PathVariable Long shopId){

        List<Review> shopReviews = customerService.getShopReviews(shopId);

        return ResponseResult.success(shopReviews);
    }
    
    
    
    
    //예약하기
    @PostMapping("/api/customer/reservation")
    public ResponseEntity<?> reservationShop(
                    @RequestHeader("C-TOKEN") String token,
                    @RequestBody @Valid ReservationShopInput reservationShopInput,
                                             Errors errors){

        String email = "";
        try{
            email = JWTUtils.getIssuer(token);
        }catch (JWTDecodeException e){
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다!", HttpStatus.BAD_REQUEST);
        }

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }


        ServiceResult result = customerService.reservationShop(email,reservationShopInput);

        return ResponseResult.result(result);
    }

    // 본인이 예약한 목록 가져오기
    @GetMapping("/api/customer/reservation")
    public ResponseEntity<?> getMyReservationList(@RequestHeader("C-TOKEN") String token){
        String email = "";
        try{
            email = JWTUtils.getIssuer(token);
        }catch (JWTDecodeException e){
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다!", HttpStatus.BAD_REQUEST);
        }

        List<Reservation> reservationList = customerService.getMyReservationList(email);

        return ResponseResult.success(reservationList);
    }
    
    

    // 키오스크 도착확인
    @PatchMapping("/api/customer/kiosk/{shopId}")
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
    @PostMapping("/api/customer/review/{reservedId}")
    public ResponseEntity<?> review(
                                    @PathVariable Long reservedId,
                                    @RequestHeader("C-TOKEN") String token,
                                    @RequestBody ReviewInput reviewInput,
                                    Errors errors
                                    ){

        String email = "";
        try{
            email = JWTUtils.getIssuer(token);
        }catch (JWTDecodeException e){
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다!", HttpStatus.BAD_REQUEST);
        }

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

        ServiceResult result = customerService.reviewShop(reservedId,email,reviewInput);

        return ResponseResult.result(result);
    }

    // 본인이 작성했던 리뷰 목록 가져오기
    @GetMapping("/api/customer/reviewed")
    public ResponseEntity<?> getMyReviewList(@RequestHeader("C-TOKEN") String token){

        String email = "";
        try{
            email = JWTUtils.getIssuer(token);
        }catch (JWTDecodeException e){
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다!", HttpStatus.BAD_REQUEST);
        }

        List<Review> myReviewList = customerService.getMyReviewList(email);

        return ResponseResult.success(myReviewList);
    }

    

}
