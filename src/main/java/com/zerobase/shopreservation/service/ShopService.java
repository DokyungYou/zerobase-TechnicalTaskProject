package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.dto.request.customer.GetShopListInput;
import com.zerobase.shopreservation.dto.response.ReviewResponse;
import com.zerobase.shopreservation.dto.response.ShopDetailResponse;
import com.zerobase.shopreservation.dto.response.ShopListResponse;

import java.util.List;

public interface ShopService {

    List<ShopListResponse> getShopList(GetShopListInput getShopListInput);

    ShopDetailResponse getShopDetail(Long shopId);

    List<ReviewResponse> getShopReviews(Long shopId);

}
