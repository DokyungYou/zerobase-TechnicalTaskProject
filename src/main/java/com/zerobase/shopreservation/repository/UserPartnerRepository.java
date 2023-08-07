package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.entity.UserPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPartnerRepository extends JpaRepository<UserPartner,Long> {

    Optional<UserPartner> findByUserId(String userId);

    Optional<UserPartner> findByBusinessRegistrationNumber(String businessRegistrationNumber);

    boolean existsByBusinessRegistrationNumber(String businessRegistrationNumber);
    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
    boolean existsByPhoneNumber(String phoneNumber);



}
