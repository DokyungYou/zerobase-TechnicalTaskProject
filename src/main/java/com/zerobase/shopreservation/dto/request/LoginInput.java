package com.zerobase.shopreservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginInput {


    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String id;


    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
