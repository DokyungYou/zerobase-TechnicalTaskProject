package com.zerobase.shopreservation.dto.type;

public enum ShopRating {

    ONE_STAR(1,"★☆☆☆☆"),
    TWO_STAR(2,"★★☆☆☆"),
    THREE_STAR(3,"★★★☆☆"),
    FOUR_STAR(4,"★★★★☆"),
    FIVE_STAR(5,"★★★★★");

    private final int value;
    private final String star;

    ShopRating(int value, String star){
        this.value = value;
        this.star = star;
    }

    public int getValue(){
        return this.value;
    }


    // 데이터베이스에 이 값으로 넣는걸로 해보자~~ ^^
    public String getStar(){
        return this.star;
    }


}
