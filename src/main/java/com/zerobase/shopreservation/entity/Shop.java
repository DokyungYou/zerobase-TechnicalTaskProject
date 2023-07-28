package com.zerobase.shopreservation.entity;


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
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


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


    @Column
    private String contactNumber;

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
    // 원래 warpper 타입으로 했다가 기본타입으로 바꿈
    @Column
    private double averageShopRating;

    @Column
    private long reviewCount;


    /**
     * 데이터 등록, 갱신
     */
    @Column
    private LocalDateTime regDate;

    @Column
    private LocalDateTime updateDate;



    @ManyToOne
    @JoinColumn
    UserPartner userPartner;


    @Column
    private String businessRegistrationNumber;


}
