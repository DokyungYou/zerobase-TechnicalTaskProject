package com.zerobase.shopreservation.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Date;

@UtilityClass
public class JWTUtils {

//
//    private static final String KEY = "fastCampus";
//    private static final String CLAIM_USER_ID = "user_id";
//
//
//    public static UserLoginToken createToken(User user){
//
//        if(user == null){
//            return null;
//        }
//
//        // Date 클래스로는 작업하기 어려운 부분이 있어서 LocalDateTime을 이용하고 이 값을 Date로 넘기기(방법은 여러가지가 있음)
//        // 유효기간은 1달로 잡음
//        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
//        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);
//
//
//        String token =  JWT.create()
//                .withExpiresAt(expiredDate)
//                .withClaim(CLAIM_USER_ID, user.getId())
//                .withSubject(user.getUserName())
//                .withIssuer(user.getEmail())
//                .sign(Algorithm.HMAC512("fastCampus".getBytes()));
//
//        return new UserLoginToken(token);
//    }
//
//
//    //이메일
//    public static String getIssuer(String token){
//
//        return JWT.require(Algorithm.HMAC512(KEY.getBytes()))
//                .build()
//                .verify(token)
//                .getIssuer();
//
//    }
}
