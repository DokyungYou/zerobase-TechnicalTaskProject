package com.zerobase.shopreservation.dto.request.customer;

import com.zerobase.shopreservation.dto.type.ShopRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewInput {


    @NotNull
    private long shopId;


    private String contents;


    @NotNull(message = "별점은 필수항목입니다.")
    private ShopRating rating;

}
