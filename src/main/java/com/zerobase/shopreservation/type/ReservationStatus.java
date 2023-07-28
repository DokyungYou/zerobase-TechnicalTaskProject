package com.zerobase.shopreservation.type;

public enum ReservationStatus {
    WAITING,
    APPROVED,
    REJECTED;

    String value;

    public String getValue() {
        return this.value;
    }


}
