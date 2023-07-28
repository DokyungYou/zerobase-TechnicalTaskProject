package com.zerobase.shopreservation.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.zerobase.shopreservation.ResponseResult;
import com.zerobase.shopreservation.common.ResponseError;
import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.input.ResponseReservationInput;
import com.zerobase.shopreservation.dto.input.partner.LoginPartnerInput;
import com.zerobase.shopreservation.dto.input.partner.ShopRegisterInput;
import com.zerobase.shopreservation.dto.input.partner.SignUpPartnerInput;

import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.service.PartnerService;
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
public class PartnerController {

    private final PartnerService partnerService;
    
    
    //테스트용
    @GetMapping("/api/partner/test")
    public String test(){
        return "왜 401이 뜨는거야";
    }
    
    
    

    // 파트너 회원가입
    @PostMapping("/api/partner/signup")
    public ResponseEntity<?> signUpPartner(
                         @RequestBody @Valid  SignUpPartnerInput signUpPartnerInput,
                         Errors errors){

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

         ServiceResult result = partnerService.signUp(signUpPartnerInput);
        if(result.isFail()){
            return ResponseResult.fail(result.getMessage());
        }

        return ResponseEntity.ok().build();
    }





    // 파트너 로그인 (흠.. 사업자 등록 번호로 로그인해야하나?, 비밀번호)
    @PostMapping("/api/partner/login")
    public ResponseEntity<?> loginPartner(@RequestBody @Valid LoginPartnerInput loginPartnerInput,
                                          Errors errors){

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

        // 토큰값을 리턴 받아야하니까 ServiceResult X
        ResponseMessage result = partnerService.login(loginPartnerInput);


        return ResponseEntity.ok().body(result);
    }


    // 상점 신규 등록 (파트너 토큰 필요)
    @PostMapping("/api/partner/shop")
    public ResponseEntity<?> shopRegister(
                            @RequestHeader("P-TOKEN") String token,
                            @RequestBody @Valid ShopRegisterInput shopRegisterInput,
                                                          Errors errors){

        //businessRegistrationNumber 로 뺏었는데 잘 안돼서 일단  email 로 해놓음
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

         ServiceResult result = partnerService.registerShop(shopRegisterInput, email);

        return ResponseResult.result(result);
    }


    // 전체 예약목록 확인(날짜순 정렬)
    @GetMapping("/api/partner/reservation/{shopId}")
    public ResponseEntity<?> reservationList(@RequestHeader("P-TOKEN") String token, @PathVariable Long shopId){

        //businessRegistrationNumber 로 뺏었는데 잘 안돼서 일단  email 로 해놓음
        String email = "";
        try{
            email = JWTUtils.getIssuer(token);
        }catch (JWTDecodeException e){
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다!", HttpStatus.BAD_REQUEST);
        }


        List<Reservation> reservationList = partnerService.getReservation(email,shopId);

        return ResponseEntity.ok().body(reservationList);
    }


    // 예약 승인 및 거절
    @PatchMapping("/api/partner/reservation/{shopId}")
    public ResponseEntity<?> responseReservation(@RequestHeader("P-TOKEN") String token,
                                                 @PathVariable Long shopId,
                                                 @RequestBody ResponseReservationInput responseReservationInput,
                                                 Errors errors){
        //예약 응답이 null 로 들어오면 대기상황인걸로 넣기

        //businessRegistrationNumber 로 뺏었는데 잘 안돼서 일단  email 로 해놓음
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


       ServiceResult result = partnerService.responseReservation(shopId,email,responseReservationInput);

        return ResponseResult.result(result);
    }
}
