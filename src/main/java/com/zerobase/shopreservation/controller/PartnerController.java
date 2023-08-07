package com.zerobase.shopreservation.controller;

import com.zerobase.shopreservation.common.ResponseError;
import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ResponseResult;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.request.LoginInput;
import com.zerobase.shopreservation.dto.request.partner.ResponseReservationInput;
import com.zerobase.shopreservation.dto.request.partner.ShopInput;
import com.zerobase.shopreservation.dto.request.partner.SignUpPartnerInput;
import com.zerobase.shopreservation.dto.request.partner.UpdateReservationDateTimeInput;
import com.zerobase.shopreservation.dto.response.ReservationResponse;
import com.zerobase.shopreservation.service.PartnerService;
import com.zerobase.shopreservation.util.JwtUtils;
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
@RequestMapping("/api/partner")
public class PartnerController {

    private final PartnerService partnerService;
    

    

    // 파트너 회원가입
    @PostMapping("/signup")
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

        return ResponseResult.success(result);
    }





    // 파트너 로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginPartner(@RequestBody @Valid LoginInput loginInput,
                                          Errors errors){

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

        // 토큰값을 리턴 받아야하니까 ServiceResult X
        ResponseMessage result = partnerService.login(loginInput);


        return ResponseEntity.ok().body(result);
    }


    // 상점 신규 등록 (파트너 토큰 필요)
    @PostMapping("/shop")
    public ResponseEntity<?> registerShop(
                            @RequestHeader("P-TOKEN") String token,
                            @RequestBody @Valid ShopInput shopInput,
                                                          Errors errors){

        String businessRegistrationNumber = JwtUtils.getBusinessRegistrationNumber(token);
        if(businessRegistrationNumber == null){
            return ResponseResult.fail("파트너 권한이 없습니다.");
        }


        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

        if(shopInput.getShopTypes().isEmpty()){
            return new ResponseEntity<>("가게유형을 1개이상 선택해야합니다.", HttpStatus.BAD_REQUEST);
        }

         ServiceResult result = partnerService.registerShop(shopInput, businessRegistrationNumber);

        return ResponseResult.result(result);
    }


    //본인 상점 정보 업데이트
    @PutMapping("/shop/{shopId}")
    public ResponseEntity<?> updateShop(
                                @RequestHeader("P-TOKEN") String token,
                                @PathVariable Long shopId,
                                @RequestBody ShopInput updateShopInput){


        String businessRegistrationNumber = JwtUtils.getBusinessRegistrationNumber(token);
        if(businessRegistrationNumber == null){
            return ResponseResult.fail("파트너 권한이 없습니다.");
        }

        ServiceResult result = partnerService.updateShop(shopId,businessRegistrationNumber,updateShopInput);

        return ResponseResult.success(result);
    }

    


    // 전체 예약목록 확인(날짜순 정렬)
    @GetMapping("/reservation/{shopId}")
    public ResponseEntity<?> reservationList(@RequestHeader("P-TOKEN") String token, @PathVariable Long shopId){

        String businessRegistrationNumber = JwtUtils.getBusinessRegistrationNumber(token);
        if(businessRegistrationNumber == null){
            return ResponseResult.fail("파트너 권한이 없습니다.");
        }

        List<ReservationResponse> reservationResponseList = partnerService.getReservation(businessRegistrationNumber,shopId);
        return ResponseEntity.ok().body(reservationResponseList);
    }


    // 예약 상태 변경 (거절, 승인, 취소, 도착, 결제완료)
    // 이미 결제완료 상태로 넣었으면 더이상 변경 불가능
    @PatchMapping("/reservation/{shopId}/status")
    public ResponseEntity<?> updateReservationStatus(
                                                 @RequestHeader("P-TOKEN") String token,
                                                 @PathVariable Long shopId,
                                                 @RequestBody @Valid ResponseReservationInput responseReservationInput,
                                                 Errors errors){

        String businessRegistrationNumber = JwtUtils.getBusinessRegistrationNumber(token);
        if(businessRegistrationNumber == null){
            return ResponseResult.fail("파트너 권한이 없습니다.");
        }

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

       ServiceResult result = partnerService.updateReservationStatus(shopId, businessRegistrationNumber, responseReservationInput);

        return ResponseResult.result(result);
    }
    
    
    // 예약날짜 변경 ( 이용자로부터의 예약날짜 변경요청은 전화 문의 받는 걸로 )
    @PatchMapping("/resevation/{shopId}/datetime")
    public ResponseEntity<?> updateReservationDateTime(
                            @RequestHeader("P-TOKEN") String token,
                            @PathVariable Long shopId,
                            @RequestBody @Valid UpdateReservationDateTimeInput updateReservationDateTimeInput,
                            Errors errors
    ){

        String businessRegistrationNumber = JwtUtils.getBusinessRegistrationNumber(token);
        if(businessRegistrationNumber == null){
            return ResponseResult.fail("파트너 권한이 없습니다.");
        }

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }

        ServiceResult result = partnerService.updateReservationDateTime(shopId, businessRegistrationNumber, updateReservationDateTimeInput);
        return ResponseResult.result(result);
    }
    

    
    // 상점 삭제 (해당 상점의 예약, 리뷰 데이터도 동시 삭제)
    @DeleteMapping("/shop/{shopId}")
    public ResponseEntity<?> deleteShop(@RequestHeader("P-TOKEN") String token,
                                        @PathVariable Long shopId){

        String businessRegistrationNumber = JwtUtils.getBusinessRegistrationNumber(token);
        if(businessRegistrationNumber == null){
            return ResponseResult.fail("파트너 권한이 없습니다.");
        }

        ServiceResult result = partnerService.deleteShop(shopId, businessRegistrationNumber);
        return ResponseResult.result(result);
    }


    // 파트너 회원탈퇴 (등록된 상점이 없을때만 가능)
    @DeleteMapping("/unregister")
    public ResponseEntity<?> unregisterPartner(@RequestHeader("P-TOKEN") String token){

        String businessRegistrationNumber = JwtUtils.getBusinessRegistrationNumber(token);
        if(businessRegistrationNumber == null){
            return ResponseResult.fail("파트너 권한이 없습니다.");
        }

        ServiceResult result = partnerService.deletePartner(businessRegistrationNumber);

        return  ResponseResult.success(result);
    }

}
