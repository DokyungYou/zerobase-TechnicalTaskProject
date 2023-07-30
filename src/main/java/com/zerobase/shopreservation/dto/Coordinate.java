package com.zerobase.shopreservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Coordinate {

    // 경도
    private double longitude;

    // 위도
    private double latitude;
    
    
    //사용할지 안할지는 모르겠으나 주소에서 좌표 뽑는 api사용하게 된다면 여기랑 연결해서 초기화시키게 하자
}
