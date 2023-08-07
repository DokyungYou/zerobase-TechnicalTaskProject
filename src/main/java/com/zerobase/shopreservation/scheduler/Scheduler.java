package com.zerobase.shopreservation.scheduler;

import com.zerobase.shopreservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {

    private final ReservationRepository reservationRepository;



    @Scheduled(cron = "0 0 0 * * *")
    public void removeInvalidReservation(){
        log.info("removing scheduler is started");
        reservationRepository.removeInvalidReservation();
        log.info("removing scheduler is completed");
    }


}
