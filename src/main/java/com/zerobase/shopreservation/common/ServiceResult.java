package com.zerobase.shopreservation.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceResult {

    private boolean result;
    private String message;

    public static ServiceResult fail(String message) {
        return ServiceResult.builder()
                .result(false)
                .message(message)
                .build();
    }

    public static ServiceResult success() {
        return ServiceResult.builder()
                .result(true)

                .build();
    }



    //귀찮아서 만든 함수..?
    public  boolean isFail(){
        return !this.result;
    }
}

