package com.zerobase.shopreservation.dto.input.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopRegisterInput {

    @NotBlank(message = "상점이름은 필수입력값입니다.")
    private String shopName;

    private String contactNumber;


    private String shopType;


    private String shopIntroduction;


    private String operatingHours;
    private String dayOff;


    private String shopAddress;
    private double longitude;
    private double latitude;


    private String seats;


    private boolean reservation;
    private String reservationType;


    private boolean takeOut;
    private boolean wifi;
    private boolean parking;
    private boolean facilitiesForDisabled;


}
