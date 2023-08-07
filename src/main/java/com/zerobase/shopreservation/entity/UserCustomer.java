package com.zerobase.shopreservation.entity;

import com.zerobase.shopreservation.dto.request.customer.SignUpCustomerInput;
import com.zerobase.shopreservation.util.PasswordUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("customer")
@SuperBuilder
public class UserCustomer extends User {

    @Column
    private String nickName;

    @OneToMany(mappedBy = "userCustomer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();



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
