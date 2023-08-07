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

}
