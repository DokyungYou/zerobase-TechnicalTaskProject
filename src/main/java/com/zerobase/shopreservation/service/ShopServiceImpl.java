package com.zerobase.shopreservation.service;

import com.zerobase.shopreservation.common.exception.BizException;
import com.zerobase.shopreservation.dto.request.customer.GetShopListInput;
import com.zerobase.shopreservation.dto.response.ReviewResponse;
import com.zerobase.shopreservation.dto.response.ShopDetailResponse;
import com.zerobase.shopreservation.dto.response.ShopListResponse;
import com.zerobase.shopreservation.dto.type.OrderByColum;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.repository.ReviewRepository;
import com.zerobase.shopreservation.repository.ShopCustomRepository;
import com.zerobase.shopreservation.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService{

    private final ShopRepository shopRepository;
    private final ShopCustomRepository shopCustomRepository;
    private final ReviewRepository reviewRepository;


    // 상점목록 가져오기
    @Override
    public List<ShopListResponse> getShopList(GetShopListInput getShopListInput) {

        // 위치정보가 들어오지 않았는데 거리관련 기능을 적용하려했을 경우
        if(getShopListInput.getCoordinate() == null){
            if(getShopListInput.getOrderByColum().equals(OrderByColum.DISTANCE)) {
                throw new BizException("거리순 정렬은 위치정보가 입력되어야 적용할 수 있습니다.");
            }
            if(getShopListInput.getMaxDistance()!= null){
                throw new BizException("거리정보는 위치정보가 입력되어야 적용할 수 있습니다.");
            }
        }

        // 위치 정보는 들어왔으나 검색반경을 따로 지정해주지 않을때는 기본값(3km)으로 초기화
        if(getShopListInput.getMaxDistance() == null || getShopListInput.getMaxDistance() <= 0){
            getShopListInput.setMaxDistance(3.0);
        }


        if(getShopListInput.getLimit() == null || getShopListInput.getLimit() <= 0){
            getShopListInput.setLimit(20);
        }

        return shopCustomRepository.getShopList(getShopListInput);
    }


    // 특정 상점 상세정보 가져오기
    @Override
    public ShopDetailResponse getShopDetail(Long shopId) {
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            throw new BizException("가게 정보가 존재하지 않습니다.");
        }

        return new ShopDetailResponse(optionalShop.get());
    }


    // 특정 상점의 리뷰리스트 가져오기
    @Override
    public List<ReviewResponse> getShopReviews(Long shopId) {
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if(optionalShop.isEmpty()){
            throw new BizException("가게 정보가 존재하지 않습니다.");
        }
        Shop shop = optionalShop.get();

        return ReviewResponse.getReviews(reviewRepository.findByShop(shop));
    }

}
