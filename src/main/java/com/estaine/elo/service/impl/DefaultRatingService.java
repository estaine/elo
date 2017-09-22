package com.estaine.elo.service.impl;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.PlayerStats;
import com.estaine.elo.repository.GameRepository;
import com.estaine.elo.repository.PlayerRepository;
import com.estaine.elo.service.RatingService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Map.Entry;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DefaultRatingService implements RatingService {

    private static final double TOURNAMENT_MULTIPLIER = 4.0 / 3.0;
    private static final double MAX_LOSING_PERCENTS = 6.0;
    private static final double GOAL_DECREASE_COEFF = 1.0 / 4.0;
    private static final double OBSOLESCENCE_STEP = 0.1;
    private static final double SKILL_CORRECTION_DEGREE = 0.2;

    private static final int WEEKS_RATED = 10;

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    @Autowired
    public DefaultRatingService(@NonNull PlayerRepository playerRepository, @NonNull GameRepository gameRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }


    @Override
    public Map<Player, PlayerStats> calculateRatings() {
        return calculateRatings(LocalDateTime.now());
    }

    @Override
    public Map<Player, PlayerStats> calculateRatings(LocalDateTime base) {

        List<Player> players = playerRepository.findAll();
        List<Game> games = gameRepository.findByPlayedOnLessThanEqualOrderByPlayedOnAsc(base);

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

            double skillCorrection = Math.pow(losersTotalRating / winnersTotalRating, SKILL_CORRECTION_DEGREE);

            int matchAgeInWeeks = getMatchAgeInWeeks(game.getPlayedOn(), base);

            double obsolescenseCoefficient = (WEEKS_RATED - matchAgeInWeeks) * OBSOLESCENCE_STEP;

            obsolescenseCoefficient = (obsolescenseCoefficient < 0 || obsolescenseCoefficient > 1) ? 0 : obsolescenseCoefficient;

            double losingPercents = tournamentMultiplier * skillCorrection * obsolescenseCoefficient
                    * (MAX_LOSING_PERCENTS - (goalsAgainst * GOAL_DECREASE_COEFF));

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


    @Override
    public Map<Player, PlayerStats> calculateRatings(LocalDateTime base, LocalDateTime start) {
        Map<Player, PlayerStats> initialRatings = calculateRatings(start);
        Map<Player, PlayerStats> finalRatings = calculateRatings(base);

        Map<Player, PlayerStats> periodRatings = new HashMap<>();

        for(Entry<Player, PlayerStats> initialEntry : initialRatings.entrySet()) {
            Player player = initialEntry.getKey();
            PlayerStats initialStats = initialEntry.getValue();
            PlayerStats finalStats = finalRatings.get(player);

            periodRatings.put(player, finalStats.subtract(initialStats));
        }

        return periodRatings;
    }

    private int getMatchAgeInWeeks(LocalDateTime matchDateTime, LocalDateTime baseDateTime) {
        LocalDate matchWeekStartDay = getWeekStart(matchDateTime);
        LocalDate baseWeekStartDay = getWeekStart(baseDateTime);

        return Period.between(matchWeekStartDay, baseWeekStartDay).getDays() / 7;
    }

    private LocalDate getWeekStart(LocalDateTime localDateTime) {
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate.with(DayOfWeek.MONDAY);
    }
}
