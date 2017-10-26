package com.estaine.elo.dto;

import com.estaine.elo.entity.Match;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class Streak {

    private static final String DATE_TIME_FORMAT = "dd-MMM-yyyy";

    private Match start;
    private Match end;
    private Integer length;

    public Streak() {
        this.length = 0;
    }

    public void increment() {
        length++;
    }

    public void reset() {
        length = 0;
    }

    @Override
    public String toString() {
        return length
                + " matches, "
                + start.getPlayedOn().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
                + " - "
                + end.getPlayedOn().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }
}
