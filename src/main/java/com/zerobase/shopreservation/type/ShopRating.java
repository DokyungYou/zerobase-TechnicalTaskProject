package com.zerobase.shopreservation.type;

public enum ShopRating {

//    NONE(0),
    ONE_STAR(1),
    TWO_STAR(2),
    THREE_STAR(3),
    FOUR_STAR(4),
    FIVE_STAR(5);

    int value;

    ShopRating(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }


}
