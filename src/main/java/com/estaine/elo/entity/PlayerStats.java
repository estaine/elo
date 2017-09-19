package com.estaine.elo.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class PlayerStats {
    private Player player;
    private List<Game> games;
    private Integer goalsFor;
    private Integer goalsAgainst;
    private BaseStats baseStats;
    private Map<Long, Boolean> results;

    public PlayerStats(Player player) {
        this.player = player;
        this.games = new ArrayList<>();
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.baseStats = new BaseStats();
        this.results = new HashMap<>();
    }

    public void updateStats(Game game) {
        games.add(game);

        boolean playerIsRed = game.getRedTeamPlayer1().equals(player) || game.getRedTeamPlayer2().equals(player);
        boolean playerIsYellow = game.getYellowTeamPlayer1().equals(player) || game.getYellowTeamPlayer2().equals(player);

        boolean redWon = game.getRedTeamGoals() > game.getYellowTeamGoals();
        boolean yellowWon = game.getYellowTeamGoals() > game.getRedTeamGoals();

        boolean win = (playerIsRed && redWon) || (playerIsYellow && yellowWon);
        results.put(game.getId(), win);

        if(playerIsRed) {
            goalsFor += game.getRedTeamGoals();
            goalsAgainst += game.getYellowTeamGoals();
        } else {
            goalsFor += game.getYellowTeamGoals();
            goalsAgainst += game.getRedTeamGoals();
        }
    }
}
