package com.zerobase.shopreservation.dto.input.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginPartnerInput {


//    @NotBlank(message = "사업자등록번호는 필수 입력값입니다.")
//    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자 등록 번호 형태가 아닙니다.")
//    private String businessRegistrationNumber;



    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String PartnerId;


    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
