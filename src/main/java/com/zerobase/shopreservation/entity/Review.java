package com.zerobase.shopreservation.entity;

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


    @Column
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private ShopRating star;


    // 만약에 가게가 폐업하면 shop데이터를 삭제해야하는데
    // 여기서 shop을 조인하고 있어서 못 지울텐데.. 흠...
    @ManyToOne
    @JoinColumn
    private Shop shop;

 
    // 아이디(Customer pk나 이메일  or 아이디)만 넣을지 UserCustomer 로 넣을지 고민
    // 근데 생각해보니 닉네임이 들어가게 해야할듯
    @ManyToOne
    @JoinColumn
    private UserCustomer userCustomer;


    @Column
    private LocalDateTime regDate;


}
