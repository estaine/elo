package com.estaine.elo.dto;

import lombok.Data;

@Data
public class EphemeralTeamStats {

    private int wins;

    private int losses;

    public EphemeralTeamStats(boolean won) {
        this.wins = 0;
        this.losses = 0;
        addResult(won);
    }

    public double getWinRate() {
        return ((double) wins) / (wins + losses);
    }

    public void addResult(boolean won) {
        if (won) {
            wins++;
        } else {
            losses++;
        }
    }
}
