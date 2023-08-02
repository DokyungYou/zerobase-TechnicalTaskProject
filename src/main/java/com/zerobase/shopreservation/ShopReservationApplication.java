package com.zerobase.shopreservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableScheduling
@SpringBootApplication
public class ShopReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopReservationApplication.class, args);
    }

}
