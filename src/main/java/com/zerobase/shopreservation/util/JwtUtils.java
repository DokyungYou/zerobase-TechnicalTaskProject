package com.zerobase.shopreservation.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zerobase.shopreservation.common.exception.BizException;
import com.zerobase.shopreservation.entity.User;
import com.zerobase.shopreservation.entity.UserPartner;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Date;


@UtilityClass
public class JwtUtils {


    private static final String KEY = "reservation_project";
    private static final String CLAIM_USER_ID = "user_id";
    private static final String BUSINESS_REGISTER_NUMBER ="businessRegistrationNumber";



    public static String createToken(User user){

        if(user == null){
            return null;
        }

        LocalDateTime expiredDateTime = LocalDateTime.now().plusHours(1);
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);


        JWTCreator.Builder builder = JWT.create()
                                    .withIssuedAt(new Date())
                                    .withExpiresAt(expiredDate)
                                    .withSubject(user.getUserName())
                                    .withIssuer(user.getEmail())
                                    .withClaim(CLAIM_USER_ID, user.getUserId());

        if(user instanceof UserPartner){
            builder.withClaim(BUSINESS_REGISTER_NUMBER,((UserPartner) user).getBusinessRegistrationNumber());
        }

        return builder.sign(Algorithm.HMAC512(KEY.getBytes()));
    }


    // 디코딩 된 토큰
    public static DecodedJWT getDecodedToken(String token) {

        try{
            return JWT.require(Algorithm.HMAC512(KEY.getBytes()))
                .build()
                .verify(token);

        }catch (SignatureVerificationException | JWTDecodeException e){
            throw new BizException("토큰 정보가 정확하지 않습니다.");
        }catch(TokenExpiredException e){
            throw new BizException("토큰 유효기간이 지났습니다.");
        }

    }
    

    // customer 유저 인증수단
    public static String getIssuer(String token) {
        return String.valueOf(JwtUtils.getDecodedToken(token).getIssuer());
    }


    // partner 유저 인증수단
    public static String getBusinessRegistrationNumber(String token){
        return JwtUtils.getDecodedToken(token).getClaim(BUSINESS_REGISTER_NUMBER).asString();

    }




}
