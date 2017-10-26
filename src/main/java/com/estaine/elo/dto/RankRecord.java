package com.estaine.elo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankRecord implements Comparable<RankRecord> {
    private Integer rank;
    private Integer totalParticipants;
    private LocalDateTime date;

    public RankRecord(Integer rank, Integer totalParticipants) {
        this.rank = rank;
        this.totalParticipants = totalParticipants;
    }

    @Override
    public int compareTo(RankRecord rankRecord) {
        if(this.rank.equals(rankRecord.rank)) {
            if(this.totalParticipants.equals(rankRecord.totalParticipants)) {
                return rankRecord.date.compareTo(this.date);
            }
            return rankRecord.totalParticipants.compareTo(this.totalParticipants);
        }
        return this.rank.compareTo(rankRecord.rank);
    }
}
