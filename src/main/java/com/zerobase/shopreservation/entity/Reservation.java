package com.zerobase.shopreservation.entity;

import com.zerobase.shopreservation.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn
    Shop shop;

    @Column
    String persons;

    @Column
    LocalDateTime reservationDateTime;

    @Column
    @Enumerated(EnumType.STRING)
    ReservationStatus reservationStatus;

    @Column
    boolean arrivedReservationTime;
}
