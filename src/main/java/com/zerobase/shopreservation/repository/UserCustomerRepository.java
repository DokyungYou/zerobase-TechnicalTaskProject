package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.entity.UserCustomer;
import com.zerobase.shopreservation.entity.UserPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCustomerRepository extends JpaRepository<UserCustomer,Long> {
    Optional<UserCustomer> findByEmail(String email);

    Optional<UserCustomer> findByCustomerId(String customerId);

    Optional<UserCustomer> findByPhoneNumber(String phoneNumber);

    Optional<UserCustomer> findByNickName(String nickName);
}
