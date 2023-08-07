package com.zerobase.shopreservation.entity;

import com.zerobase.shopreservation.common.exception.BizException;
import com.zerobase.shopreservation.dto.request.customer.ReservationShopInput;
import com.zerobase.shopreservation.dto.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn
    private UserCustomer userCustomer;


    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
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


    // 예약요청등록일시
    @Column
    private LocalDateTime regDate;

    // 예약상태가 변경된 시점
    @Column
    private LocalDateTime statusUpdate;



    public static Reservation reservationShop(ReservationShopInput reservationShopInput, Shop shop, UserCustomer userCustomer){

        LocalDateTime localDateTime = Reservation.FromDateString(reservationShopInput.getReservationDateTime());
        if(localDateTime.isBefore(LocalDateTime.now())){
            throw new BizException("예약이 가능한 날짜가 아닙니다. (과거의 날짜 및 시간)");
        }
        if(localDateTime.isBefore(LocalDate.now().plusDays(1).atStartOfDay())){
            throw new BizException("예약이 가능한 날짜가 아닙니다. (당일 예약은 불가합니다.)");
        }


        return
                Reservation.builder()
                        .shop(shop)
                        .userCustomer(userCustomer)
                        .reservationContents(reservationShopInput.getReservationContents())
                        .reservationDateTime(localDateTime)
                        .phoneNumber(userCustomer.getPhoneNumber())
                        .regDate(LocalDateTime.now())
                        .reservationStatus(ReservationStatus.WAITING)
                        .build();
    }

    public static LocalDateTime FromDateString(String dateTimeString){

        LocalDateTime localDateTime = null;
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            localDateTime = LocalDateTime.parse(dateTimeString,formatter);
        }catch (DateTimeException e){
            throw new BizException("예약이 가능한 날짜 및 시간 형식이 아닙니다.");
        }
        return localDateTime;
    }

}
