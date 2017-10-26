package com.estaine.elo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseStats {

    public static final double INITIAL_RATING = 10_000.0;

    private Double rating;
    private int matchesPlayed;

    public BaseStats() {
        this.rating = INITIAL_RATING;
        matchesPlayed = 0;
    }

    public void updateRating(double rating) {
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
