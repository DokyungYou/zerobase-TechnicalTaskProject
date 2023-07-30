package com.zerobase.shopreservation.type;

public enum  ShopType {

    KOREAN_FOOD("한식"),
    CHINESE_FOOD("중식"),
    WESTERN_FOOD("양식"),
    JAPANESE_FOOD("일식"),
    THAILAND_FOOD("태국음식"),
    ETC("등등?");

    private String typeName;


    ShopType(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName(){
        return this.typeName;
    }

}
