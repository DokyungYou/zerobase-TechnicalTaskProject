package com.zerobase.shopreservation.dto.response;

import com.zerobase.shopreservation.entity.Shop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopDetailResponse {

    private long id;


    private String shopName;


    private String shopType;



    private String shopIntroduction;

    private String contactNumber;

    private String shopAddress;



    private String operatingHours; //운영시간, 요일

    private String dayOff;


    private boolean bookable;


    private String reservationType;


    private String seats;



    private boolean takeOut;


    private boolean wifi;


    private boolean parking;


    private boolean facilitiesForDisabled;




    private double averageShopRating;
    private long reviewCount;



    private LocalDateTime regDate;
    private LocalDateTime updateDate;


    private String partnerName;
    private String businessRegistrationNumber;



    public ShopDetailResponse(Shop shop){
        this.id= shop.getId();
        this.shopName= shop.getShopName();
        this.shopType= shop.getShopType();
        this.shopIntroduction= shop.getShopIntroduction();
        this.contactNumber= shop.getContactNumber();
        this.shopAddress= shop.getShopAddress();
        this.operatingHours= shop.getOperatingHours();
        this.dayOff= shop.getDayOff();
        this.bookable= shop.isBookable();
        this.reservationType= shop.getReservationType();
        this.seats= shop.getSeats();
        this.takeOut= shop.isTakeOut();
        this.wifi= shop.isWifi();
        this.parking= shop.isParking();
        this.facilitiesForDisabled= shop.isFacilitiesForDisabled();
        this.averageShopRating = Math.round(shop.getAverageShopRating() * 10)/10.0;
        this.reviewCount= shop.getReviewCount();
        this.regDate= shop.getRegDate();
        this.updateDate= shop.getUpdateDate();
        this.partnerName= shop.getUserPartner().getUserName();
        this.businessRegistrationNumber= shop.getBusinessRegistrationNumber();
    }

}
