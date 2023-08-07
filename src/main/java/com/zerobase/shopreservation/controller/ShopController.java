package com.zerobase.shopreservation.controller;

import com.zerobase.shopreservation.common.ResponseResult;
import com.zerobase.shopreservation.dto.request.customer.GetShopListInput;
import com.zerobase.shopreservation.dto.response.ReviewResponse;
import com.zerobase.shopreservation.dto.response.ShopDetailResponse;
import com.zerobase.shopreservation.dto.response.ShopListResponse;
import com.zerobase.shopreservation.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class ShopController {

    private final ShopService shopService;

    // 상점목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> getShopList(@RequestBody GetShopListInput getShopListInput){


        List<ShopListResponse> shopListResponse = shopService.getShopList(getShopListInput);

        return ResponseResult.success(shopListResponse);
    }


    // 특정상점 detail 정보 조회
    @GetMapping("/{shopId}")
    public ResponseEntity<?> getShopDetail(@PathVariable Long shopId){

        ShopDetailResponse shop = shopService.getShopDetail(shopId);
        return ResponseResult.success(shop);
    }


    // 특정 상점 리뷰목록 조회
    @GetMapping("/{shopId}/review")
    public ResponseEntity<?> getShopReviews(@PathVariable Long shopId){

        List<ReviewResponse> shopReviewResponses = shopService.getShopReviews(shopId);
        return ResponseResult.success(shopReviewResponses);
    }

}
