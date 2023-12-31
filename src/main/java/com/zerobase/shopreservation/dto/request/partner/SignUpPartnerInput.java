package com.zerobase.shopreservation.dto.request.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class
SignUpPartnerInput {

    @NotBlank(message = "사업자등록번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자 등록 번호 형태가 아닙니다.")
    private String businessRegistrationNumber;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String partnerId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,15}", message = "비밀번호는 8~15자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;


    //수정필요
    @Size(max = 20, message = "연락처는 최대 20자까지만 유효합니다.")
    @NotBlank(message = "전화번호는 필수 항목 입니다!")
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$|^\\d{3}-\\d{3}-\\d{4}$", message = "유효하지 않은 핸드폰 번호 형식입니다.")
    private String phoneNumber;



}
