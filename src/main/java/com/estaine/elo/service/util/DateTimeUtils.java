package com.estaine.elo.service.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import org.springframework.stereotype.Component;

@Component
public class DateTimeUtils {

    public int calculateTimePeriodInWeeks(LocalDateTime from, LocalDateTime to) {
        LocalDate matchWeekStartDay = getWeekStart(from);
        LocalDate baseWeekStartDay = getWeekStart(to);

        return Period.between(matchWeekStartDay, baseWeekStartDay).getDays() / 7;
    }

    public LocalDate getWeekStart(LocalDateTime localDateTime) {
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate.with(DayOfWeek.MONDAY);
    }
}
