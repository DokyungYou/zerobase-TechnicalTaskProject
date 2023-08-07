package com.zerobase.shopreservation.dto.request.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationShopInput {


    @NotNull(message = "상점 아이디는 필수 입력값입니다.")
    private Long shopId;

    // 예약인원, 특이사항 등의 내용
    @NotBlank(message = "예약내용은 필수 입력값입니다.")
    private String reservationContents;



    @NotBlank(message = "예약날짜는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "올바른 형식의 날짜와 시간을 입력해주세요. (yyyy-MM-dd HH:mm:ss)")
    private String reservationDateTime;

}
