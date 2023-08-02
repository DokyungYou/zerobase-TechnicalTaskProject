package com.zerobase.shopreservation.entity;

import com.zerobase.shopreservation.dto.input.partner.SignUpPartnerInput;
import com.zerobase.shopreservation.util.PasswordUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("partner")
public class UserPartner extends User{

    @Column //10자리 (xxx-xx-xxxxx)
    private String businessRegistrationNumber;


    public static UserPartner createUserPartner(SignUpPartnerInput signUpPartnerInput){

        String encryptPassword = PasswordUtils.getEncryptPassword(signUpPartnerInput.getPassword());

        return UserPartner.builder()
                            .businessRegistrationNumber(signUpPartnerInput.getBusinessRegistrationNumber())
                            .userName(signUpPartnerInput.getName())
                            .phoneNumber(signUpPartnerInput.getPhoneNumber())
                            .email(signUpPartnerInput.getEmail())
                            .userId(signUpPartnerInput.getPartnerId())
                            .password(encryptPassword)
                            .signUpDate(LocalDateTime.now())
                            .build();
    }

}
