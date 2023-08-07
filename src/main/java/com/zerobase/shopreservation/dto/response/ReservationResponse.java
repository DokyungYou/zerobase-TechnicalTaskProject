package com.zerobase.shopreservation.dto.response;

import com.zerobase.shopreservation.dto.type.ReservationStatus;
import com.zerobase.shopreservation.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {



    private Long reservationId;


    private Long customerId;


    private Long shopId;

    private String shopName;



    // 예약인원, 특이사항 등의 내용
    private String reservationContents;


    private String phoneNumber;


    private LocalDateTime reservationDateTime;


    private ReservationStatus reservationStatus;


    // 예약요청등록
    private LocalDateTime regDate;

    // 상태변경된 시점
    private LocalDateTime statusUpdate;


    public static List<ReservationResponse> getReservationList(List<Reservation> reservationEntities){
       return reservationEntities.stream().map(
               reservation -> ReservationResponse.builder()
                       .reservationId(reservation.getId())
                       .customerId(reservation.getUserCustomer().getId())
                       .shopId(reservation.getShop().getId())
                       .shopName(reservation.getShop().getShopName())
                       .reservationContents(reservation.getReservationContents())
                       .phoneNumber(reservation.getPhoneNumber())
                       .reservationDateTime(reservation.getReservationDateTime())
                       .reservationStatus(reservation.getReservationStatus())
                       .regDate(reservation.getRegDate())
                       .statusUpdate(reservation.getStatusUpdate())
                       .build())
               .collect(Collectors.toList());
    }

}
