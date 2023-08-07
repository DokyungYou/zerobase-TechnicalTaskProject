package com.zerobase.shopreservation.entity;

import com.zerobase.shopreservation.dto.request.customer.ReviewInput;
import com.zerobase.shopreservation.dto.type.ShopRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(length = 500)
    private String content;

    
    @Column
    @Enumerated(EnumType.STRING)
    private ShopRating rating;


    @Column
    private String ratingStar;



    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Shop shop;


    @Column
    private Long reservedId;


    // 리뷰는 이용자가 회원탈퇴해도 남게끔 참조키를 사용하지 않았음
    @Column
    private Long userId;

    @Column
    private String userNickname;


    @Column
    private LocalDateTime regDate;


    public static Review writeReview(Long reservedId, ReviewInput reviewInput, Shop shop, UserCustomer userCustomer){
        return Review.builder()
                        .content(reviewInput.getContents())
                        .shop(shop)
                        .reservedId(reservedId)

                        .userId(userCustomer.getId())  //pk
                        .userNickname(userCustomer.getUserId())
                        .regDate(LocalDateTime.now())
                        .rating(reviewInput.getRating())
                        .ratingStar(reviewInput.getRating().getStar())
                        .build();
    }
}
