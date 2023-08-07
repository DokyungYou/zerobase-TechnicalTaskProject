package com.zerobase.shopreservation.dto.request.partner;

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

    @NotNull(message = "예약 상태는 필수입력값입니다.")
    private ReservationStatus reservationStatus;


}
