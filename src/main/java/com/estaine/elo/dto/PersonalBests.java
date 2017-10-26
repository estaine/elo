package com.estaine.elo.dto;

import lombok.Data;

@Data
public class PersonalBests {
    private RatingRecord highestRating;
    private RatingRecord lowestRating;
    private RankRecord highestRank;
    private RankRecord lowestRank;
    private Streak longestWinStreak;
    private Streak longestLoseStreak;

    public PersonalBests() {
        this.highestRating = new RatingRecord();
        this.lowestRating = new RatingRecord();
        this.highestRank = new RankRecord();
        this.lowestRank = new RankRecord();
        this.longestWinStreak = new Streak();
        this.longestLoseStreak = new Streak();
    }
}
