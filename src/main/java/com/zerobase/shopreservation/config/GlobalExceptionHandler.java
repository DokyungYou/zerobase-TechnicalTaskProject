package com.zerobase.shopreservation.config;

import com.zerobase.shopreservation.common.exception.BizException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //임시로 BizException 으로 잡아 놓은거 상황에 맞게 종류별로 예외 만들어놓자.
    @org.springframework.web.bind.annotation.ExceptionHandler(BizException.class)
    public ResponseEntity<?> handlerBizException(BizException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
