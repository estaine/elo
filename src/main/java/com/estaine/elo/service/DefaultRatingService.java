package com.estaine.elo.service;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.PlayerStats;
import com.estaine.elo.repository.GameRepository;
import com.estaine.elo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DefaultRatingService implements RatingService {

    private static final double TOURNAMENT_MULTIPLIER = 4.0 / 3.0;
    private static final double MAX_LOSING_PERCENTS = 8.0;
    private static final double GOAL_DECREASE_COEFF = 1.0 / 3.0;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public Map<Player, PlayerStats> calculateRatings() {

        List<Player> players = playerRepository.findAll();
        List<Game> games = gameRepository.findAllByOrderByPlayedOnAsc();

        Map<Player, PlayerStats> statsMap = new HashMap<>();

        for (Player player : players) {
            statsMap.put(player, new PlayerStats(player));
        }

        for (Game game : games) {
            Player winner1 = (game.getRedTeamGoals() > game.getYellowTeamGoals()) ? game.getRedTeamPlayer1() : game.getYellowTeamPlayer1();
            Player winner2 = (winner1 == game.getRedTeamPlayer1()) ? game.getRedTeamPlayer2() : game.getYellowTeamPlayer2();
            Player loser1 = (winner1 == game.getRedTeamPlayer1()) ? game.getYellowTeamPlayer1() : game.getRedTeamPlayer1();
            Player loser2 = (winner1 == game.getRedTeamPlayer1()) ? game.getYellowTeamPlayer2() : game.getRedTeamPlayer2();

            int goalsAgainst = (winner1 == game.getRedTeamPlayer1()) ? game.getYellowTeamGoals() : game.getRedTeamGoals();

            double tournamentMultiplier = game.isTournamentGame() ? TOURNAMENT_MULTIPLIER : 1;

            double winnersTotalRating = statsMap.get(winner1).getBaseStats().getRating() + statsMap.get(winner2).getBaseStats().getRating();
            double losersTotalRating = statsMap.get(loser1).getBaseStats().getRating() + statsMap.get(loser2).getBaseStats().getRating();

            double skillCorrection = Math.sqrt(losersTotalRating / winnersTotalRating);

            double losingPercents = tournamentMultiplier * skillCorrection * (MAX_LOSING_PERCENTS - (goalsAgainst * GOAL_DECREASE_COEFF));

            double loser1Delta = statsMap.get(loser1).getBaseStats().getRating() * losingPercents / 100.0;
            double loser2Delta = statsMap.get(loser2).getBaseStats().getRating() * losingPercents / 100.0;
            double winnerDelta = (loser1Delta + loser2Delta) / 2.0;

            statsMap.get(winner1).getBaseStats().updateRating(statsMap.get(winner1).getBaseStats().getRating() + winnerDelta);
            statsMap.get(winner2).getBaseStats().updateRating(statsMap.get(winner2).getBaseStats().getRating() + winnerDelta);
            statsMap.get(loser1).getBaseStats().updateRating(statsMap.get(loser1).getBaseStats().getRating() - loser1Delta);
            statsMap.get(loser2).getBaseStats().updateRating(statsMap.get(loser2).getBaseStats().getRating() - loser2Delta);

            statsMap.get(winner1).updateStats(game);
            statsMap.get(winner2).updateStats(game);
            statsMap.get(loser1).updateStats(game);
            statsMap.get(loser2).updateStats(game);

            statsMap.get(winner1).getRatingDelta().put(game.getId(), winnerDelta);
            statsMap.get(winner2).getRatingDelta().put(game.getId(), winnerDelta);
            statsMap.get(loser1).getRatingDelta().put(game.getId(), loser1Delta);
            statsMap.get(loser2).getRatingDelta().put(game.getId(), loser2Delta);

        }

        return statsMap;
    }
}
