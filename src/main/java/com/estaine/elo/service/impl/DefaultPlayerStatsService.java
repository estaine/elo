package com.estaine.elo.service.impl;

import com.estaine.elo.dto.PersonalBests;
import com.estaine.elo.dto.PlayerStats;
import com.estaine.elo.dto.Streak;
import com.estaine.elo.entity.Match;
import com.estaine.elo.entity.Player;
import com.estaine.elo.repository.MatchRepository;
import com.estaine.elo.repository.PlayerRepository;
import com.estaine.elo.service.PlayerStatsService;
import com.estaine.elo.service.RatingService;
import java.util.List;
import lombok.NonNull;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultPlayerStatsService implements PlayerStatsService {

    @Value("${significance.threshold}")
    private int significanceThreshold;

    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final RatingService ratingService;

    @Autowired
    public DefaultPlayerStatsService(@NonNull PlayerRepository playerRepository,
            @NonNull MatchRepository matchRepository,
            @NonNull RatingService ratingService) {
        this.playerRepository = playerRepository;
        this.ratingService = ratingService;
        this.matchRepository = matchRepository;
    }

    @Override
    public PlayerStats getPlayerStats(String username) {
        Player player = playerRepository.findByUsername(username);
        PlayerStats playerStats = ratingService.calculateRatings().get(player);
        Hibernate.initialize(playerStats.getPlayer().getAwards());

        List<Match> playerMatches = matchRepository.findAllByPlayer(player);

        if(playerMatches.size() >= significanceThreshold) {
            playerStats.setPersonalBests(new PersonalBests());
            playerStats.getPersonalBests().setLongestWinStreak(calculateLongestStreak(player, playerMatches, true));
            playerStats.getPersonalBests().setLongestLossStreak(calculateLongestStreak(player, playerMatches, false));

            playerStats = ratingService.calculateRecords(playerStats);
        }

        return playerStats;
    }


    private Streak calculateLongestStreak(Player player, List<Match> matches, boolean result) {
        Streak longestStreak = new Streak();
        Streak streak = new Streak();

        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            if (match.isWonBy(player) == result) {
                if (streak.getLength() == 0) {
                    streak.setStart(match);
                }
                streak.increment();
            } else if ((i > 0) && (matches.get(i - 1).isWonBy(player) == result)) {
                streak.setEnd(matches.get(i - 1));
                if (streak.getLength() >= longestStreak.getLength()) {
                    longestStreak = streak;
                }
                streak = new Streak();
            }
        }

        if (streak.getLength() >= longestStreak.getLength()) {
            streak.setEnd(matches.get(matches.size() - 1));
            longestStreak = streak;
        }

        return longestStreak;
    }

}
