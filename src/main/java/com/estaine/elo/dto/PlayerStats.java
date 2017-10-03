package com.estaine.elo.dto;

import com.estaine.elo.entity.Match;
import com.estaine.elo.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class PlayerStats {
    private Player player;
    private List<Match> matches;
    private Integer goalsFor;
    private Integer goalsAgainst;
    private Integer wins;
    private Integer losses;
    private BaseStats baseStats;
    private Map<Long, Boolean> results;
    private Map<Long, Double> ratingDelta;

    public PlayerStats(Player player) {
        this.player = player;
        this.matches = new ArrayList<>();
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.wins = 0;
        this.losses = 0;
        this.baseStats = new BaseStats();
        this.results = new HashMap<>();
        this.ratingDelta = new HashMap<>();
    }

    public void updateStats(Match match) {
        matches.add(match);

        boolean playerIsRed = match.getRedTeamPlayer1().equals(player) || match.getRedTeamPlayer2().equals(player);
        boolean playerIsYellow = match.getYellowTeamPlayer1().equals(player) || match.getYellowTeamPlayer2().equals(player);

        boolean redWon = match.getRedTeamGoals() > match.getYellowTeamGoals();
        boolean yellowWon = match.getYellowTeamGoals() > match.getRedTeamGoals();

        boolean win = (playerIsRed && redWon) || (playerIsYellow && yellowWon);
        results.put(match.getId(), win);

        if(playerIsRed) {
            goalsFor += match.getRedTeamGoals();
            goalsAgainst += match.getYellowTeamGoals();
        } else {
            goalsFor += match.getYellowTeamGoals();
            goalsAgainst += match.getRedTeamGoals();
        }

        if(win) {
            wins++;
        } else {
            losses++;
        }
    }

    public PlayerStats subtract(PlayerStats playerStats) {
        PlayerStats diff = new PlayerStats(this.player);

        diff.matches.addAll(this.matches);
        diff.matches.removeAll(playerStats.matches);

        diff.goalsFor = this.goalsFor - playerStats.goalsFor;
        diff.goalsAgainst = this.goalsAgainst - playerStats.goalsAgainst;
        diff.wins = this.wins - playerStats.wins;
        diff.losses = this.losses - playerStats.losses;
        diff.baseStats = this.baseStats.subtract(playerStats.baseStats);

        this.results.forEach(diff.results::putIfAbsent);
        playerStats.results.forEach(diff.results::remove);

        this.ratingDelta.forEach(diff.ratingDelta::putIfAbsent);
        playerStats.ratingDelta.forEach(diff.ratingDelta::remove);

        return diff;
    }

    @Override
    public String toString() {
        return player.toString() + "[" + String.format("%.2f", baseStats.getRating()) + "]";
    }
}
