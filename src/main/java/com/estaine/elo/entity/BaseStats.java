package com.estaine.elo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseStats {

    private Double rating;
    private int gamesPlayed;

    public void updateRating(double rating) {
        this.rating = rating;
        gamesPlayed++;
    }
}
