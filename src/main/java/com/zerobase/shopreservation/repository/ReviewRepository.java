package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.entity.Review;
import com.zerobase.shopreservation.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

}
