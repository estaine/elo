package com.estaine.elo.service;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.PlayerStats;
import com.estaine.elo.repository.GameRepository;
import com.estaine.elo.repository.PlayerRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPlayerStatsService implements PlayerStatsService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    RatingService ratingService;

    @Override
    public PlayerStats getPlayerStats(String username) {
        Player player = playerRepository.findByUsername(username);
        List<Game> playerGames = gameRepository.findAllByPlayer(player);

        PlayerStats playerStats = new PlayerStats();
        playerStats.setPlayer(player);
        playerStats.setGames(playerGames);
        playerStats.setBaseStats(ratingService.calculateRatings().get(player));

        Map<Long, Boolean> results = new HashMap<>();

        int goalsFor = 0;
        int goalsAgainst = 0;

        for(Game game : playerGames) {
            int forDelta;
            int againstDelta;

            if(game.getRedTeamPlayer1().equals(player) || game.getRedTeamPlayer2().equals(player)) {
                forDelta = game.getRedTeamGoals();
                againstDelta = game.getYellowTeamGoals();
            } else {
                forDelta = game.getYellowTeamGoals();
                againstDelta = game.getRedTeamGoals();
            }

            goalsFor += forDelta;
            goalsAgainst += againstDelta;

            results.put(game.getId(), forDelta > againstDelta);
        }

        playerStats.setGoalsFor(goalsFor);
        playerStats.setGoalsAgainst(goalsAgainst);
        playerStats.setResults(results);

        return playerStats;
    }
}
