package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.entity.Review;
import com.zerobase.shopreservation.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByUserId(Long userId);


    // 나중에 shop pk로 바꿀수도 있음
    List<Review> findByShop(Shop shop);


    Optional<Review> findByUserIdAndShopAndReservedId(Long userId, Shop shop, Long reservedId);

    boolean existsByUserIdAndShopAndReservedId(Long userId, Shop shop, Long reservedId);

}
