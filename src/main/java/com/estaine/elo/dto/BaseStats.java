package com.estaine.elo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseStats {

    public static final double INITIAL_RATING = 10_000.0;

    private Double rating;
    private int matchesPlayed;
    private int matchesRated;

    public BaseStats() {
        this.rating = INITIAL_RATING;
        matchesPlayed = 0;
        matchesRated = 0;
    }

    public void updateRating(double rating) {
        if(!this.rating.equals(rating)) {
            matchesRated++;
        }

        this.rating = rating;
        matchesPlayed++;
    }

    public BaseStats subtract(BaseStats baseStats) {
        BaseStats diff = new BaseStats();

        diff.rating = this.rating - baseStats.rating;
        diff.matchesPlayed = this.matchesPlayed - baseStats.matchesPlayed;

        return diff;
    }
}
