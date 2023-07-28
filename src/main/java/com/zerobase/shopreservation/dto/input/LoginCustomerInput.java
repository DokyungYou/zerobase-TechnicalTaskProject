package com.zerobase.shopreservation.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginCustomerInput {


    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String customerId;


    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;


}
