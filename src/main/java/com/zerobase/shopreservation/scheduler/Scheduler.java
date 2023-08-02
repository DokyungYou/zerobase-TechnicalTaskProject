package com.zerobase.shopreservation.scheduler;

import com.zerobase.shopreservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Scheduler {

    private final ReservationRepository reservationRepository;


    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 청소
    public void removeInvalidReservation(){
        reservationRepository.removeInvalidReservation();
    }


}
