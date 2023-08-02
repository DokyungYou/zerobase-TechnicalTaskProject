package com.zerobase.shopreservation.dto.input;

import com.zerobase.shopreservation.dto.type.Authority;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class Auth {

    @Data
    public static class SignIn{

        @NotBlank(message = "아이디는 필수 입력값입니다.")
        private String customerId;


        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        private String password;
    }

    @Data
    public static class SignUp{
        private String userId;
        private String password;
        private Authority roles;  //권한

    }
}
