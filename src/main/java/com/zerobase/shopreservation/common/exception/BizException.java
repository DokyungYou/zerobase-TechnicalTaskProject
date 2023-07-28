package com.zerobase.shopreservation.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BizException extends RuntimeException {
    public BizException(String message) {
        super(message);
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<?> handlerBizException(BizException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
