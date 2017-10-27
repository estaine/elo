package com.estaine.elo.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankRecord {

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private Integer rank;
    private Integer totalParticipants;
    private LocalDateTime date;

    public RankRecord(Integer rank, Integer totalParticipants) {
        this.rank = rank;
        this.totalParticipants = totalParticipants;
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
}
