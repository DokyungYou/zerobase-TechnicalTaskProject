package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.entity.Review;
import com.zerobase.shopreservation.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findByShopOrderByReservationDateTime(Shop shop);

    Optional<Reservation> findByIdAndShop(Long id, Shop shop);

}
