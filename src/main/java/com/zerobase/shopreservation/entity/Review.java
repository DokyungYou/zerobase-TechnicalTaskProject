package com.zerobase.shopreservation.entity;

import com.zerobase.shopreservation.type.ShopRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column
    private String text;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private ShopRating star;


    @ManyToOne
    @JoinColumn
    private Shop shop;

 
    // 아이디만 넣을지 클래스로 넣을지 고민
    @ManyToOne
    @JoinColumn
    private UserCustomer userCustomer;


    @Column
    private LocalDateTime regDate;

}
