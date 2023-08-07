package com.zerobase.shopreservation.repository;

import com.zerobase.shopreservation.entity.UserCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCustomerRepository extends JpaRepository<UserCustomer,Long> {

    Optional<UserCustomer> findByEmail(String email);

    Optional<UserCustomer> findByUserId(String customerId);


    @Query("select count(c) from UserCustomer c")
    Long countAll();



    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByNickName(String nickname);
}
