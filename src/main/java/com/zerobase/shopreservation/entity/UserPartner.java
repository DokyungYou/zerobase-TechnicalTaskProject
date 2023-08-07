package com.zerobase.shopreservation.entity;

import com.zerobase.shopreservation.dto.request.partner.SignUpPartnerInput;
import com.zerobase.shopreservation.util.PasswordUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("partner")
@SuperBuilder
public class UserPartner extends User {

    @Column //10자리 (xxx-xx-xxxxx)
    private String businessRegistrationNumber;


//    @OneToMany(mappedBy = "userPartner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private List<Shop> reservations = new ArrayList<>();



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
