package com.zerobase.shopreservation.dto.type;

public enum ReservationStatus {
    WAITING("대기"),
    APPROVED("승인"),
    REJECTED("거부"),
    ARRIVED("도착"),
    CANCELLATION("취소"),
    COMPLETED_PAYMENT("결제 완료")
    ;

    private final String value;


    ReservationStatus(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
