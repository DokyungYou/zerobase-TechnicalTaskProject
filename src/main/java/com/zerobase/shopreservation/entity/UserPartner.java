package com.zerobase.shopreservation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPartner {

    // 유니크 키로 넣고, 따로 예외처리를 할까, 넣지 말고 if(데이터 존재){return false}형식으로 할까 고민중

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //
    @Column
    private String email;


    @Column(nullable = false)
    private String partnerId;

    //
    @Column(nullable = false)
    private String password;

    //
    @Column(nullable = false)
    private String partnerName;


    @Column
    private String phoneNumber;


    //
    @Column //10자리 (xxx-xx-xxxxx)
    private String businessRegistrationNumber;


    @Column
    private LocalDateTime signUpDate;


    //이거는 고민중
    @Column
    private String accountStatus;



//    //이게 맞나?
//    @OneToMany
//    private List<Shop> shop;



}
