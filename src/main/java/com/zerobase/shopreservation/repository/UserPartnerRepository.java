package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.entity.UserPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPartnerRepository extends JpaRepository<UserPartner,Long> {

    Optional<UserPartner> findByEmail(String email);

    Optional<UserPartner> findByPartnerId(String partnerId);

    Optional<UserPartner> findByPhoneNumber(String phoneNumber);

    Optional<UserPartner> findByBusinessRegistrationNumber(String businessRegistrationNumber);


}
