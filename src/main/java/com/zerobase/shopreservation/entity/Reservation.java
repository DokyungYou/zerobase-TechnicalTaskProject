package com.zerobase.shopreservation.entity;

import com.zerobase.shopreservation.dto.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 토큰에서 빼서 넣기?
    @ManyToOne
    @JoinColumn
    private UserCustomer userCustomer;


    @ManyToOne
    @JoinColumn
    private Shop shop;


    // 예약인원, 특이사항 등의 내용
    @Column
    private String reservationContents;


    @Column
    private String phoneNumber;


    @Column
    private LocalDateTime reservationDateTime;


    @Column
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;


    // 예약요청등록
    @Column
    private LocalDateTime regDate;

    // 승인 및 거절한 시점
    @Column
    private LocalDateTime statusUpdate;


}
