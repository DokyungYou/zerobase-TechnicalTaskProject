package com.zerobase.shopreservation.dto.response;

import com.zerobase.shopreservation.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {


    private Long id;

    private String userNickname;

    private String content;

    private String ratingStar;


    private Long shopId;
    private String shopName;


    private LocalDateTime regDate;


    public static List<ReviewResponse> getReviews(List<Review> reviewEntities){

        return reviewEntities.stream().map(review -> ReviewResponse.builder()
                                    .id(review.getId())
                                    .userNickname(review.getUserNickname())
                                    .content(review.getContent())
                                    .ratingStar(review.getRatingStar())
                                    .shopId(review.getShop().getId())
                                    .shopName(review.getShop().getShopName())
                                    .regDate(review.getRegDate())
                                    .build())
                .collect(Collectors.toList());
    }
}
