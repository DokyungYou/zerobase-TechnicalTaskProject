package com.zerobase.shopreservation.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zerobase.shopreservation.entity.UserCustomer;
import com.zerobase.shopreservation.entity.UserPartner;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Date;

@UtilityClass
public class JWTUtils {


    private static final String KEY = "zerobase";
    private static final String CLAIM_USER_ID = "user_id";


    //이걸 보니까 파트너, 이용자 유저를 한 인터페이스나 클래스 상속하게 하고 넣어야할듯...?
    //일단은 파트너 전용으로 만들어놓자
    //임시로 반환타입 String으로 잡음
    public static String createToken(UserPartner user){

        if(user == null){
            return null;
        }

        // Date 클래스로는 작업하기 어려운 부분이 있어서 LocalDateTime을 이용하고 이 값을 Date로 넘기기(방법은 여러가지가 있음)
        // 유효기간은 1시간으로 잡음
        // 토큰 값은 수정해야할듯
        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);


        String token =  JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("business_registration_number", user.getBusinessRegistrationNumber())
                .withSubject(user.getPartnerName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("zerobase".getBytes()));

        return token;
    }


    public static String createCustomerToken(UserCustomer user){

        if(user == null){
            return null;
        }

        // Date 클래스로는 작업하기 어려운 부분이 있어서 LocalDateTime을 이용하고 이 값을 Date로 넘기기(방법은 여러가지가 있음)
        // 유효기간은 1시간으로 잡을건데 (만드는동안은 불편하니까 1달로 잡자)
        // 토큰 값은 수정해야할듯
        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);


        String token =  JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("customer_id", user.getCustomerId())
                .withSubject(user.getCustomerName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("zerobase".getBytes()));

        return token;
    }



    //이메일
    public static String getIssuer(String token){

        return JWT.require(Algorithm.HMAC512(KEY.getBytes()))
                .build()
                .verify(token)
                .getIssuer();

    }

    public static String getBusinessRegistrationNumber(String token){

        return String.valueOf(JWT.require(Algorithm.HMAC512(KEY.getBytes()))
                                    .build()
                                    .verify(token)
                                    .getClaim("business_registration_number"));

    }
}
