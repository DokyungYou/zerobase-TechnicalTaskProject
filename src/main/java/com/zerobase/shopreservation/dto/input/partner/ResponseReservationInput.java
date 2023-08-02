package com.zerobase.shopreservation.dto.input.partner;

import com.zerobase.shopreservation.dto.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseReservationInput {


    @NotNull(message = "예약 아이디는 필수입력값입니다.")
    private Long reservationId;

    @NotNull   // 보니까 여기에 어노테이션 안 붙여놨던데 안됐었나?
    private ReservationStatus reservationStatus;


}
