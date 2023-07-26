package com.zerobase.shopreservation.dto.input;

import javax.validation.constraints.NotBlank;

public class LoginPartnerInput {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String PartnerId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
