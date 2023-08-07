package com.zerobase.shopreservation.entity;


import com.zerobase.shopreservation.dto.request.partner.ShopInput;
import com.zerobase.shopreservation.dto.type.ShopRating;
import com.zerobase.shopreservation.dto.type.ShopType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @Column(nullable = false, length = 500)
    private String shopName;



    @Column
    private String shopType;



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
    private boolean bookable;

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
    @JoinColumn(nullable = false)
    UserPartner userPartner;


    // 조회 클래스를 따로 만들때 엔티티클래스에 있는 UserPartner에 있는 사업자번호 따로 빼도 될텐데, 이건 여기서는 없어도 될려나
    @Column
    private String businessRegistrationNumber;





    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();



    // 상점 신규 등록
    public static Shop registerShop(ShopInput shopInput, UserPartner partner){

        return Shop.builder()
                    .takeOut(shopInput.isTakeOut())
                    .wifi(shopInput.isWifi())
                    .parking(shopInput.isParking())
                    .facilitiesForDisabled(shopInput.isFacilitiesForDisabled())
                    .seats(shopInput.getSeats())
                    .bookable(shopInput.isBookable())
                    .reservationType(shopInput.getReservationType())
                    .operatingHours(shopInput.getOperatingHours())
                    .dayOff(shopInput.getDayOff())
                    .shopAddress(shopInput.getShopAddress())


                    .longitude(shopInput.getCoordinate().getLongitude())
                    .latitude(shopInput.getCoordinate().getLatitude())

                    .contactNumber(shopInput.getContactNumber())
                    .shopIntroduction(shopInput.getShopIntroduction())

                    .shopName(shopInput.getShopName())


                    .shopType(Shop.getStringShopTypes(shopInput.getShopTypes()))


                    .reviewCount(0L)
                    .averageShopRating(0.0)


                    .businessRegistrationNumber(partner.getBusinessRegistrationNumber())
                    .regDate(LocalDateTime.now())
                    .userPartner(partner)
                    .build();
    }


    public static Shop updateShop(ShopInput shopInput, Shop shop){

        shop.setTakeOut(shopInput.isTakeOut());
        shop.setWifi(shopInput.isWifi());
        shop.setParking(shopInput.isParking());
        shop.setFacilitiesForDisabled(shopInput.isFacilitiesForDisabled());
        shop.setSeats(shopInput.getSeats());
        shop.setBookable(shopInput.isBookable());
        shop.setReservationType(shopInput.getReservationType());
        shop.setOperatingHours(shopInput.getOperatingHours());
        shop.setDayOff(shopInput.getDayOff());
        shop.setShopAddress(shopInput.getShopAddress());

        shop.setLongitude(shopInput.getCoordinate().getLongitude());
        shop.setLatitude(shopInput.getCoordinate().getLatitude());

        shop.setContactNumber(shopInput.getContactNumber());
        shop.setShopIntroduction(shopInput.getShopIntroduction());

        shop.setShopName(shopInput.getShopName());
        shop.setShopType(Shop.getStringShopTypes(shopInput.getShopTypes()));

        shop.setBusinessRegistrationNumber(shopInput.getBusinessRegistrationNumber());
        shop.setUpdateDate(LocalDateTime.now());

       // shop.setUserPartner(partner);

        return shop;

    }

    static String getStringShopTypes(List<ShopType> shopTypes){

        if(shopTypes.size() == 1){
            return shopTypes.get(0).getTypeName();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < shopTypes.size(); i++) {
            if(i != shopTypes.size()-1){
                sb.append(shopTypes.get(i).getTypeName()).append(",");
            }else{
                sb.append(shopTypes.get(i).getTypeName());
            }
        }
        return sb.toString();
    }




    public void updateShopRating(Shop shop, ShopRating shopRating){

        long beforeReviewCount = shop.getReviewCount();
        long newReviewCount = beforeReviewCount + 1;

        double beforeRating = shop.getAverageShopRating();
        double newRating = ((beforeRating * beforeReviewCount) + shopRating.getValue()) / newReviewCount;

        shop.setReviewCount(newReviewCount);
        shop.setAverageShopRating(newRating);

    }


}
