package com.estaine.elo.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingRecord {

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private Double value;
    private LocalDateTime date;

    public RatingRecord() {
        value = BaseStats.INITIAL_RATING;
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
