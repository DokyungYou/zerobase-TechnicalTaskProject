package com.zerobase.shopreservation.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateTime {

    private int year;

    private int month;

    private int dayOfMonth;

    private int time;

}
