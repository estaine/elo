package com.estaine.elo.service.impl;

import com.estaine.elo.dto.BaseStats;
import com.estaine.elo.dto.PersonalBests;
import com.estaine.elo.dto.PlayerStats;
import com.estaine.elo.dto.RankRecord;
import com.estaine.elo.dto.RatingRecord;
import com.estaine.elo.entity.Match;
import com.estaine.elo.entity.Player;
import com.estaine.elo.format.RatingFormatter;
import com.estaine.elo.repository.MatchRepository;
import com.estaine.elo.repository.PlayerRepository;
import com.estaine.elo.service.RatingService;
import com.estaine.elo.service.util.DateTimeUtils;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class DefaultRatingService implements RatingService {

    private static final double TOURNAMENT_MULTIPLIER = 4.0 / 3.0;
    private static final double MAX_LOSING_PERCENTS = 6.0;
    private static final double GOAL_DECREASE_COEFF = 1.0 / 4.0;
    private static final double OBSOLESCENCE_STEP = 0.1;
    private static final double SKILL_CORRECTION_DEGREE = 0.2;

    private static final int WEEKS_RATED = 10;

    @Value("${significance.threshold}")
    private int significanceThreshold;

    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final DateTimeUtils dateTimeUtils;
    private final RatingFormatter ratingFormatter;

    @Autowired
    public DefaultRatingService(@NonNull PlayerRepository playerRepository, @NonNull MatchRepository matchRepository, @NonNull DateTimeUtils dateTimeUtils, @NonNull RatingFormatter ratingFormatter) {
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.dateTimeUtils = dateTimeUtils;
        this.ratingFormatter = ratingFormatter;
    }


    @Override
    public Map<Player, PlayerStats> calculateRatings() {
        return calculateRatings(LocalDateTime.now());
    }

    @Override
    public Map<Player, PlayerStats> calculateRatings(LocalDateTime base) {

        List<Player> players = playerRepository.findAll();
        List<Match> matches = matchRepository.findByPlayedOnLessThanEqualOrderByPlayedOnAsc(base);

        Map<Player, PlayerStats> statsMap = new HashMap<>();

        for (Player player : players) {
            statsMap.put(player, new PlayerStats(player));
        }

        for (Match match : matches) {
            Player winner1 = (match.getRedTeamGoals() > match.getYellowTeamGoals()) ? match.getRedTeamPlayer1() : match.getYellowTeamPlayer1();
            Player winner2 = (winner1 == match.getRedTeamPlayer1()) ? match.getRedTeamPlayer2() : match.getYellowTeamPlayer2();
            Player loser1 = (winner1 == match.getRedTeamPlayer1()) ? match.getYellowTeamPlayer1() : match.getRedTeamPlayer1();
            Player loser2 = (winner1 == match.getRedTeamPlayer1()) ? match.getYellowTeamPlayer2() : match.getRedTeamPlayer2();

            double losingPercents = calculateLosingPercents(match, statsMap, base);

            updateRatings(statsMap, match, winner1, winner2, loser1, loser2, losingPercents, true);

            statsMap.get(winner1).updateStats(match);
            statsMap.get(winner2).updateStats(match);
            statsMap.get(loser1).updateStats(match);
            statsMap.get(loser2).updateStats(match);
        }

        return statsMap;
    }


    @Override
    public Map<Player, PlayerStats> calculateRatings(LocalDateTime base, LocalDateTime start) {
        Map<Player, PlayerStats> initialRatings = calculateRatings(start);
        Map<Player, PlayerStats> finalRatings = calculateRatings(base);

        Map<Player, PlayerStats> periodRatings = new HashMap<>();

        for (Entry<Player, PlayerStats> initialEntry : initialRatings.entrySet()) {
            Player player = initialEntry.getKey();
            PlayerStats initialStats = initialEntry.getValue();
            PlayerStats finalStats = finalRatings.get(player);

            periodRatings.put(player, finalStats.subtract(initialStats));
        }

        return periodRatings;
    }

    @Override
    public PlayerStats calculateRecords(PlayerStats playerStats) {
        Player player = playerStats.getPlayer();
        List<Player> players = playerRepository.findAll();
        List<Match> matches = matchRepository.findByPlayedOnLessThanEqualOrderByPlayedOnAsc(LocalDateTime.now());

        RatingRecord highestRating = new RatingRecord();
        RatingRecord lowestRating = new RatingRecord();
        RankRecord highestRank = new RankRecord(Integer.MAX_VALUE, Integer.MAX_VALUE);
        RankRecord lowestRank = new RankRecord(0, 0);

        for (Match lastMatch : matches) {
            List<Match> precedingMatches = matches.stream()
                    .filter(m -> m.getPlayedOn().isBefore(lastMatch.getPlayedOn()) || m.getPlayedOn().isEqual(lastMatch.getPlayedOn()))
                    .collect(Collectors.toList());

            Map<Player, PlayerStats> statsMap = new HashMap<>();

            for (Player p : players) {
                statsMap.put(p, new PlayerStats(p));
            }

            for (Match match : precedingMatches) {
                Player winner1 = (match.getRedTeamGoals() > match.getYellowTeamGoals()) ? match.getRedTeamPlayer1() : match.getYellowTeamPlayer1();
                Player winner2 = (winner1 == match.getRedTeamPlayer1()) ? match.getRedTeamPlayer2() : match.getYellowTeamPlayer2();
                Player loser1 = (winner1 == match.getRedTeamPlayer1()) ? match.getYellowTeamPlayer1() : match.getRedTeamPlayer1();
                Player loser2 = (winner1 == match.getRedTeamPlayer1()) ? match.getYellowTeamPlayer2() : match.getRedTeamPlayer2();

                double losingPercents = calculateLosingPercents(match, statsMap, lastMatch.getPlayedOn());

                updateRatings(statsMap, match, winner1, winner2, loser1, loser2, losingPercents, false);

                RatingRecord currentRating = new RatingRecord(statsMap.get(player).getBaseStats().getRating(), match.getPlayedOn());
                RankRecord currentRank = null;

                List<PlayerStats> sortedResults = ratingFormatter.sortRating(statsMap);

                for (int i = 0; i < sortedResults.size(); i++) {
                    PlayerStats stats = sortedResults.get(i);
                    if (stats.getPlayer().getId().equals(player.getId())) {
                        currentRank = new RankRecord(i + 1, sortedResults.size(), match.getPlayedOn());
                        break;
                    }
                }

                if (currentRating.getValue() >= highestRating.getValue()) {
                    highestRating = currentRating;
                }

                if (currentRating.getValue() <= lowestRating.getValue()) {
                    lowestRating = currentRating;
                }

                if (currentRank != null) {
                    if(currentRank.getRank() < highestRank.getRank()) {
                        highestRank = currentRank;
                    } else if(currentRank.getRank().equals(highestRank.getRank())
                            && (currentRank.getTotalParticipants() >= highestRank.getTotalParticipants())) {
                        highestRank = currentRank;
                    }

                    if(currentRank.getRank() > lowestRank.getRank()) {
                        lowestRank = currentRank;
                    } else if(currentRank.getRank().equals(lowestRank.getRank())
                            && (currentRank.getTotalParticipants() >= lowestRank.getTotalParticipants())) {
                        lowestRank = currentRank;
                    }
                }

            }
        }
        playerStats.getPersonalBests().setHighestRank(highestRank);
        playerStats.getPersonalBests().setLowestRank(lowestRank);
        playerStats.getPersonalBests().setHighestRating(highestRating);
        playerStats.getPersonalBests().setLowestRating(lowestRating);

        return playerStats;
    }

    private double calculateLosingPercents(Match match, Map<Player, PlayerStats> statsMap, LocalDateTime base) {
        Player winner1 = (match.getRedTeamGoals() > match.getYellowTeamGoals()) ? match.getRedTeamPlayer1() : match.getYellowTeamPlayer1();
        Player winner2 = (winner1 == match.getRedTeamPlayer1()) ? match.getRedTeamPlayer2() : match.getYellowTeamPlayer2();
        Player loser1 = (winner1 == match.getRedTeamPlayer1()) ? match.getYellowTeamPlayer1() : match.getRedTeamPlayer1();
        Player loser2 = (winner1 == match.getRedTeamPlayer1()) ? match.getYellowTeamPlayer2() : match.getRedTeamPlayer2();

        int goalsAgainst = (winner1 == match.getRedTeamPlayer1()) ? match.getYellowTeamGoals() : match.getRedTeamGoals();

        double tournamentMultiplier = match.isTournamentMatch() ? TOURNAMENT_MULTIPLIER : 1;

        double winnersTotalRating = statsMap.get(winner1).getBaseStats().getRating() + statsMap.get(winner2).getBaseStats().getRating();
        double losersTotalRating = statsMap.get(loser1).getBaseStats().getRating() + statsMap.get(loser2).getBaseStats().getRating();

        double skillCorrection = Math.pow(losersTotalRating / winnersTotalRating, SKILL_CORRECTION_DEGREE);

        int matchAgeInWeeks = dateTimeUtils.calculateTimePeriodInWeeks(match.getPlayedOn(), base);
        double obsolescenseCoefficient = (WEEKS_RATED - matchAgeInWeeks) * OBSOLESCENCE_STEP;
        obsolescenseCoefficient = (obsolescenseCoefficient < 0 || obsolescenseCoefficient > 1) ? 0 : obsolescenseCoefficient;

        return tournamentMultiplier * skillCorrection * obsolescenseCoefficient * (MAX_LOSING_PERCENTS - (goalsAgainst * GOAL_DECREASE_COEFF));
    }

    private void updateRatings(Map<Player, PlayerStats> statsMap, Match match,
            Player winner1, Player winner2, Player loser1, Player loser2,
            double losingPercents, boolean saveDeltas) {

        double loser1Delta = statsMap.get(loser1).getBaseStats().getRating() * losingPercents / 100.0;
        double loser2Delta = statsMap.get(loser2).getBaseStats().getRating() * losingPercents / 100.0;
        double winnerDelta = (loser1Delta + loser2Delta) / 2.0;

        statsMap.get(winner1).getBaseStats().updateRating(statsMap.get(winner1).getBaseStats().getRating() + winnerDelta);
        statsMap.get(winner2).getBaseStats().updateRating(statsMap.get(winner2).getBaseStats().getRating() + winnerDelta);
        statsMap.get(loser1).getBaseStats().updateRating(statsMap.get(loser1).getBaseStats().getRating() - loser1Delta);
        statsMap.get(loser2).getBaseStats().updateRating(statsMap.get(loser2).getBaseStats().getRating() - loser2Delta);

        if (saveDeltas) {
            statsMap.get(winner1).getRatingDelta().put(match.getId(), winnerDelta);
            statsMap.get(winner2).getRatingDelta().put(match.getId(), winnerDelta);
            statsMap.get(loser1).getRatingDelta().put(match.getId(), loser1Delta);
            statsMap.get(loser2).getRatingDelta().put(match.getId(), loser2Delta);
        }
    }
}
