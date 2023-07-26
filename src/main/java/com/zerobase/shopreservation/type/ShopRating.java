package com.zerobase.shopreservation.type;

public enum ShopRating {

    NONE,
    ONE_STAR,
    TWO_STAR,
    THREE_STAR,
    FOUR_STAR,
    FIVE_STAR;

    int value;

    public int getValue(){
        return this.value;
    }
}
