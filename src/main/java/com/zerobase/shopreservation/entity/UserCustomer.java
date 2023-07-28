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

    // 유니크 키로 넣고, 따로 예외처리를 할까, 넣지 말고 if(데이터 존재){return false}형식으로 할까 고민중

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String customerId;


    @Column
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



    @Column
    private String nickName;


    //계정 상태를 넣을까 말까 생각중
}
