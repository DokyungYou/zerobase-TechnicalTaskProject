package com.zerobase.shopreservation.dto.type;

public enum  ShopType {

    KOREAN_FOOD("한식"),
    CHINESE_FOOD("중식"),
    WESTERN_FOOD("양식"),
    JAPANESE_FOOD("일식"),
    THAILAND_FOOD("태국음식"),
    FUSION_FOOD("퓨전요리"),
    MEXICAN_FOOD("멕시코요리"),
    ETC("기타요리");

    private final String typeName;


    ShopType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName(){
        return this.typeName;
    }

}
