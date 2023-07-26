package com.zerobase.shopreservation.controller;

import com.zerobase.shopreservation.ResponseResult;
import com.zerobase.shopreservation.common.ResponseError;
import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.input.SignUpCustomerInput;
import com.zerobase.shopreservation.dto.input.SignUpPartnerInput;
import com.zerobase.shopreservation.service.CustomerService;
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
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/customer/signup")
    public ResponseEntity<?> signUpPartner(
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
}
