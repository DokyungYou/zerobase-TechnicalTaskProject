package com.zerobase.shopreservation.dto.input.partner;

import com.zerobase.shopreservation.dto.Coordinate;
import com.zerobase.shopreservation.type.ShopType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopRegisterInput {

    @NotBlank(message = "상점이름은 필수입력값입니다.")
    private String shopName;

    @NotBlank(message = "대표번호는 필수입력값입니다.")
    private String contactNumber;

    
    //수정할수도 있음
    // List 타입 열거형클래스 받고, 그걸 나중에 문자열로 나열해서 문자열로 저장하게할까..
//    private String shopType;
    @NotNull(message = "가게타입은 필수입력값입니다.")
    private List<ShopType> shopTypes;


    private String shopIntroduction;


    private String operatingHours;
    private String dayOff;



    @NotBlank(message = "주소는 필수입력값입니다.")
    private String shopAddress;

    @NotNull(message = "위치정보는 필수입력값입니다.")
    private Coordinate coordinate;



    private String seats;


    private boolean bookable;
    private String reservationType;


    private boolean takeOut;
    private boolean wifi;
    private boolean parking;
    private boolean facilitiesForDisabled;


}
