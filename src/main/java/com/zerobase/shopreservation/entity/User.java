package com.zerobase.shopreservation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE", discriminatorType = DiscriminatorType.STRING)
@MappedSuperclass
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String userId;


    @Column(nullable = false)
    private String password;


    @Column
    private String userName;


    @Column(nullable = false)
    private String phoneNumber;


    @Column
    private LocalDateTime signUpDate;


    //이 부분은 좀 더 고민 (회원정보 수정 기능은 넣을지 말지 고민중)
    @Column
    private LocalDateTime updateDate;



}
