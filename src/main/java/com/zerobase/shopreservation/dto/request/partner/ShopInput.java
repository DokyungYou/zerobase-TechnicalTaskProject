package com.zerobase.shopreservation.dto.request.partner;

import com.zerobase.shopreservation.dto.Coordinate;
import com.zerobase.shopreservation.dto.type.ShopType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopInput {


    @NotBlank(message = "상점이름은 필수입력값입니다.")
    private String shopName;

    @NotBlank(message = "대표번호는 필수입력값입니다.")
    private String contactNumber;

    


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

    
    // 로그인한 계정의 사업자등록번호와 일치하는지 확인
    private String businessRegistrationNumber;


}
