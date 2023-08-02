package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.entity.Review;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.entity.UserCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByUserCustomer(UserCustomer customer);


    // 나중에 shop pk로 바꿀수도 있음
    List<Review> findByShop(Shop shop);
}
