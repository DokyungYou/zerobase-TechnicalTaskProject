package com.zerobase.shopreservation.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  "header":
 {
 result: true|false
 , resultCode: string
 , message: error message or alert message
 , status: http result code
 },
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseMessageHeader {

    private boolean result;
    private String resultCode;
    private String message;
    private int status; //임시
}
