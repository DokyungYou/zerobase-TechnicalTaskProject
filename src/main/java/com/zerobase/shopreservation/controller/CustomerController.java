package com.zerobase.shopreservation.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.zerobase.shopreservation.ResponseResult;
import com.zerobase.shopreservation.common.ResponseError;
import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.GetShopList;
import com.zerobase.shopreservation.dto.input.*;
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
        if(result.isFail()){
            return ResponseResult.fail(result.getMessage());
        }

        return ResponseEntity.ok().build();
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

        return ResponseEntity.ok().body(shopList);
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


}
