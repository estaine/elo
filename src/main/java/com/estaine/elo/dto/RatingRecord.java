package com.estaine.elo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RatingRecord implements Comparable<RatingRecord> {
    private Double value;
    private LocalDateTime date;

    public RatingRecord() {
        value = BaseStats.INITIAL_RATING;
    }

    @Override
    public int compareTo(RatingRecord ratingRecord) {
        if(this.value.equals(ratingRecord.value)) {
            return ratingRecord.date.compareTo(this.date);
        }
        return this.value.compareTo(ratingRecord.value);
    }
}
