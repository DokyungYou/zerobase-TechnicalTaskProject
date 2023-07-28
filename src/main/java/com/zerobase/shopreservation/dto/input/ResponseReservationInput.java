package com.zerobase.shopreservation.dto.input;

import com.zerobase.shopreservation.type.ReservationStatus;
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

    private ReservationStatus reservationStatus;


}
