package com.zerobase.shopreservation.controller;

import com.zerobase.shopreservation.ResponseResult;
import com.zerobase.shopreservation.common.ResponseError;
import com.zerobase.shopreservation.common.ResponseMessage;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.input.LoginPartnerInput;
import com.zerobase.shopreservation.dto.input.SignUpPartnerInput;

import com.zerobase.shopreservation.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    // 파트너 회원가입
    @PostMapping("/partner/signup")
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
    //이거 리퀘스트메소드 뭘로 해야지..
    public ResponseEntity<?> loginPartner(@RequestBody @Valid LoginPartnerInput loginPartnerInput,
                                          Errors errors){

        if(errors.hasErrors()){
            List<ObjectError> allErrors = errors.getAllErrors();
            return ResponseEntity.badRequest().body(ResponseError.of(allErrors));
        }


        return null;
    }
}
