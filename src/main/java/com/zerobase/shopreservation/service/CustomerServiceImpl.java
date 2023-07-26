package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.input.SignUpCustomerInput;
import com.zerobase.shopreservation.entity.UserCustomer;

import com.zerobase.shopreservation.repository.UserCustomerRepository;
import com.zerobase.shopreservation.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final UserCustomerRepository userCustomerRepository;

    @Override
    public ServiceResult signUp(SignUpCustomerInput signUpCustomerInput) {
        //아이디, 이메일, 연락처 중복체크
        Optional<UserCustomer> byEmail = userCustomerRepository.findByEmail(signUpCustomerInput.getEmail());
        if(byEmail.isPresent()){
            return ServiceResult.fail("입력하신 이메일은 이미 사용중입니다.");
        }
        Optional<UserCustomer> byPartnerId = userCustomerRepository.findByCustomerId(signUpCustomerInput.getCustomerId());
        if(byPartnerId.isPresent()){
            return ServiceResult.fail("입력하신 아이디는 이미 사용중입니다.");
        }


        // 연락처는 좀 더 생각해보자
        Optional<UserCustomer> byPhoneNumber = userCustomerRepository.findByPhoneNumber(signUpCustomerInput.getPhoneNumber());
        if(byPhoneNumber.isPresent()){
            return ServiceResult.fail("입력하신 휴대전화번호는 이미 사용중입니다.");
        }


        //입력한 비밀번호 암호화
        String encryptPassword = PasswordUtils.getEncryptPassword(signUpCustomerInput.getPassword());


        UserCustomer customer = UserCustomer.builder()
                            .customerName(signUpCustomerInput.getName())
                            .phoneNumber(signUpCustomerInput.getPhoneNumber())
                            .email(signUpCustomerInput.getEmail())
                            .customerId(signUpCustomerInput.getCustomerId())
                            .password(encryptPassword)
                            .nickName(signUpCustomerInput.getNickname())
                            .signUpDate(LocalDateTime.now())
                            .build();

        userCustomerRepository.save(customer);
        return ServiceResult.success();
    }

}
