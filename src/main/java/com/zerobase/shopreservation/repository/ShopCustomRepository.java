package com.zerobase.shopreservation.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ShopCustomRepository {

    private final EntityManager entityManager;
}
