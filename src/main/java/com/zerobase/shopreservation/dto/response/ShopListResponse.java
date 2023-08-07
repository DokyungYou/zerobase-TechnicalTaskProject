package com.zerobase.shopreservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopListResponse {

    private Long id;

    private String shopName;

    // 상점타입의 타입은 나중에 수정할수도 있음
    private String shopType;

    private double average_shop_rating;

    private long reviewCount;

    private boolean bookable;

    private double distance;

    // java.lang.RuntimeException: No constructor taking: (db에 있는 타입이랑 달라서 생성자 작업이 따로 필요함)
    public ShopListResponse(BigInteger id, String shopName, String shopType, Double average_shop_rating, BigInteger reviewCount, Boolean bookable, Double distance){

        this.id = id.longValue();
        this.shopName = shopName;
        this.shopType = shopType;
        this.average_shop_rating = Math.round(average_shop_rating * 10)/10.0;
        this.reviewCount = reviewCount.longValue();
        this.bookable = bookable;
        this.distance = Math.round(distance* 10)/10.0;

    }
}
