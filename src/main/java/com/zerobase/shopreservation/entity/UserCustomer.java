package com.zerobase.shopreservation.entity;

import com.zerobase.shopreservation.dto.input.SignUpCustomerInput;
import com.zerobase.shopreservation.util.PasswordUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("customer")
@SuperBuilder
public class UserCustomer extends User{

    @Column
    private String nickName;


    public static UserCustomer createUserCustomer(SignUpCustomerInput signUpCustomerInput){

        String encryptPassword = PasswordUtils.getEncryptPassword(signUpCustomerInput.getPassword());

        return  UserCustomer.builder()
                            .userName(signUpCustomerInput.getName())
                            .phoneNumber(signUpCustomerInput.getPhoneNumber())
                            .email(signUpCustomerInput.getEmail())
                            .userId(signUpCustomerInput.getCustomerId())
                            .password(encryptPassword)
                            .nickName(signUpCustomerInput.getNickname())
                            .signUpDate(LocalDateTime.now())
                            .build();
    }


}
