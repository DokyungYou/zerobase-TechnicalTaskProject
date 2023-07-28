package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.entity.UserPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {

    Long countById(Long shopId);

}
