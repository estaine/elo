package com.estaine.elo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseStats {

    private static final double INITIAL_RATING = 10_000.0;

    private Double rating;
    private int gamesPlayed;

    public BaseStats() {
        this.rating = INITIAL_RATING;
        gamesPlayed = 0;
    }

    public void updateRating(double rating) {
        this.rating = rating;
        gamesPlayed++;
    }
}
