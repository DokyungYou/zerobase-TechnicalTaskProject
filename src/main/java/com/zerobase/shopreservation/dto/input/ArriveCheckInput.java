package com.zerobase.shopreservation.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArriveCheckInput {

    // 키오스크에서는 좀 더 간편하게 사용가능해야하니까
    // 로그인을 핸드폰 번호로 할까

    @NotBlank(message = "핸드폰 번호는 필수 입력값입니다.")
    private String phoneNumber;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;


}
