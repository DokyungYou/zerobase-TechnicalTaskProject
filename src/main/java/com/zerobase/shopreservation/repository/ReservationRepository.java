package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.entity.Reservation;
import com.zerobase.shopreservation.entity.Shop;
import com.zerobase.shopreservation.entity.UserCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findByShopOrderByReservationDateTime(Shop shop);

    Optional<Reservation> findByIdAndShop(Long id, Shop shop);

    Optional<Reservation> findByShopAndPhoneNumber(Shop shop, String phoneNumber);

    List<Reservation> findByUserCustomer(UserCustomer userCustomer);


    Optional<Reservation> findByIdAndUserCustomer(Long reservationId, UserCustomer userCustomer);


    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "delete from reservation " +
                    "where (reservation_date_time <= now() " +
                    "and (reservation_status = 'COMPLETED_PAYMENT' and now() > reservation_date_time + interval 3 day) " +
                    "or (reservation_status != 'COMPLETED_PAYMENT')) " +
                    "or reservation_status = 'CANCELLATION';")
    void removeInvalidReservation();


}
