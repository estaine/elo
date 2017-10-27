package com.estaine.elo.dto;

import com.estaine.elo.entity.Match;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class Streak {

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private Match start;
    private Match end;
    private Integer length;

    public Streak() {
        this.length = 0;
    }

    public void increment() {
        length++;
    }

    public String getFormattedStart() {
        return start.getPlayedOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public String getFormattedEnd() {
        return end.getPlayedOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    @Override
    public String toString() {
        return length
                + " matches, "
                + start.getPlayedOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                + " - "
                + end.getPlayedOn().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
