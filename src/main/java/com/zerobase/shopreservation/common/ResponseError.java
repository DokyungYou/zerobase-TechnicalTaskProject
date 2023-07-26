package com.zerobase.shopreservation.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseError {

    private String field;
    private String message; // 어떠한 필드에 에러가 났다라는 메세지?


    public static ResponseError of(FieldError e){
        return  ResponseError.builder()
                .field(e.getField())
                .message(e.getDefaultMessage())
                .build();
    }



    // List<ObjectError> allErrors = errors.getAllErrors();
    public static List<ResponseError> of(List<ObjectError> errors){
        List<ResponseError> responseErrors = new ArrayList<>();

        if(errors != null){
            errors.forEach(e -> responseErrors.add(ResponseError.of((FieldError) e)));
        }

        return responseErrors;
    }
}
