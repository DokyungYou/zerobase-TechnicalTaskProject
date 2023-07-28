package com.zerobase.shopreservation.dto.input;

import com.zerobase.shopreservation.type.ShopRating;
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


    
    // 사실 이건 넣어야할지 말지 고민중
    @NotNull
    private long shopId;


    private String contents;


    @NotNull //여기에 쓸 수 있나?
    private ShopRating rating;

}
