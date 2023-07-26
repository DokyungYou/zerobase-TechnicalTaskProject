package com.zerobase.shopreservation.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 가게 기본정보
     */
    @Column(nullable = false)
    private String shopName;

    @Column
    private String shopType;

//    @Enumerated(EnumType.STRING)  //걍 List<String>으로 받을까 생각중
//    ShopType type;

    @Column
    private String shopIntroduction;


    /**
     * 가게 위치
     */
    @Column
    private String shopAddress;

    @Column
    private double longitude;

    @Column
    private double latitude;

    /**
     * 운영일
     */
    @Column
    private String operatingHours; //운영시간, 요일

    @Column
    private String dayOff;


    /**
     * 예약관련
     */
    @Column
    private boolean reservation;

    @Column
    private String reservationType;

    @Column
    private String seats;


    /**
     * 가능 여부
     */
    @Column
    private boolean takeOut;

    @Column
    private boolean wifi;

    @Column
    private boolean parking;

    @Column
    private boolean facilitiesForDisabled;


    /**
     * 별점, 리뷰수
     */
    @Column
    private double averageShopRating;

    @Column
    private Long reviewCount;


    /**
     * 데이터 등록, 갱신
     */
    @Column
    private LocalDateTime regDate;

    @Column
    private LocalDateTime updateDate;


}
