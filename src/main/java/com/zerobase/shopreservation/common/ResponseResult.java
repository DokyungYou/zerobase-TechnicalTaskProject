package com.zerobase.shopreservation.common;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ResponseResult {

    public static ResponseEntity<?> success(){
        return success(null);
    }


    public static ResponseEntity<?> success(Object data){

        return ResponseEntity.ok().body(ResponseMessage.success(data));
    }



    public static ResponseEntity<?> fail(String message){

        return  ResponseEntity.badRequest().body(ResponseMessage.fail(message));
    }



    public static ResponseEntity<?> fail(String message, Object data){

        return  ResponseEntity.badRequest().body(ResponseMessage.fail(message,data));
    }



    public static ResponseEntity<?> result(ServiceResult result) {

        if(result.isFail()){
            return fail(result.getMessage());
        }

        return  success();
    }

}
