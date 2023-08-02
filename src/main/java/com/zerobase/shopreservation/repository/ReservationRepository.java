package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.entity.Review;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.entity.UserCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findByShopOrderByReservationDateTime(Shop shop);

    Optional<Reservation> findByIdAndShop(Long id, Shop shop);

    Optional<Reservation> findByPhoneNumber(String phoneNumber);

    List<Reservation> findByUserCustomer(UserCustomer userCustomer);



    @Modifying
    @Query(nativeQuery = true,
            value = "delete from reservation " +
                    "where reservation_date_time <= now() " +
                    "and (reservation_status = 'CARRIED_OUT' and now() > reservation_date_time + interval 3 day) " +
                    "or (reservation_status != 'CARRIED_OUT');")
    void removeInvalidReservation();


}
