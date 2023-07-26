package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.ServiceResult;
import com.zerobase.shopreservation.dto.input.SignUpPartnerInput;
import com.zerobase.shopreservation.entity.UserPartner;
import com.zerobase.shopreservation.repository.UserPartnerRepository;
import com.zerobase.shopreservation.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService{

    private final UserPartnerRepository userPartnerRepository;

    // 파트너 회원가입
    @Override
    public ServiceResult signUp(SignUpPartnerInput signUpPartnerInput) {

        //아이디, 이메일, 연락처 중복체크
        Optional<UserPartner> byEmail = userPartnerRepository.findByEmail(signUpPartnerInput.getEmail());
        if(byEmail.isPresent()){
            return ServiceResult.fail("입력하신 이메일은 이미 사용중입니다.");
        }
        Optional<UserPartner> byPartnerId = userPartnerRepository.findByPartnerId(signUpPartnerInput.getPartnerId());
        if(byPartnerId.isPresent()){
            return ServiceResult.fail("입력하신 아이디는 이미 사용중입니다.");
        }


        // 연락처는 좀 더 생각해보자
        Optional<UserPartner> byPhoneNumber = userPartnerRepository.findByPhoneNumber(signUpPartnerInput.getPhoneNumber());
        if(byPhoneNumber.isPresent()){
            return ServiceResult.fail("입력하신 휴대전화번호는 이미 사용중입니다.");
        }


        //입력한 비밀번호 암호화
        String encryptPassword = PasswordUtils.getEncryptPassword(signUpPartnerInput.getPassword());

        
        UserPartner partner = UserPartner.builder()
                    .businessRegistrationNumber(signUpPartnerInput.getBusinessRegistrationNumber())
                    .partnerName(signUpPartnerInput.getName())
                    .phoneNumber(signUpPartnerInput.getPhoneNumber())
                    .email(signUpPartnerInput.getEmail())
                    .partnerId(signUpPartnerInput.getPartnerId())
                    .password(encryptPassword)
                    .signUpDate(LocalDateTime.now())
                    .build();

        userPartnerRepository.save(partner);
        return ServiceResult.success();
    }
}
