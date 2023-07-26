package com.zerobase.shopreservation.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String customerId;


    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    @Column
    private String customerName;


    @Column
    private String phoneNumber;


    @Column
    private LocalDateTime signUpDate;

    
    //이 부분은 좀 더 고민
    @Column
    private LocalDateTime updateDate;


    @Column(unique = true)
    private String nickName;


    //계정 상태를 넣을까 말까 생각중
}
