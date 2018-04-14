package com.estaine.elo.service.impl;

import com.estaine.elo.dto.EphemeralTeam;
import com.estaine.elo.dto.EphemeralTeamStats;
import com.estaine.elo.dto.PartnerStats;
import com.estaine.elo.dto.PartnerStats.PartnerStatsPopularityComparator;
import com.estaine.elo.dto.PartnerStats.PartnerStatsWinRateComparator;
import com.estaine.elo.dto.PersonalBests;
import com.estaine.elo.dto.PlayerStats;
import com.estaine.elo.dto.Streak;
import com.estaine.elo.entity.Match;
import com.estaine.elo.entity.Player;
import com.estaine.elo.repository.MatchRepository;
import com.estaine.elo.repository.PlayerRepository;
import com.estaine.elo.service.PlayerStatsService;
import com.estaine.elo.service.RatingService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultPlayerStatsService implements PlayerStatsService {

    @Value("${significance.threshold}")
    private int significanceThreshold;

    private final static int PARTNER_THRESHOLD = 10;

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

        List<PartnerStats> allPartnerStats = getPartnerStats(player, playerMatches);

        allPartnerStats.sort(new PartnerStatsWinRateComparator());
        playerStats.setBestPercentagePartner(allPartnerStats.get(0));
        playerStats.setWorstPercentagePartner(allPartnerStats.get(allPartnerStats.size() - 1));

        allPartnerStats.sort(new PartnerStatsPopularityComparator());
        playerStats.setFavouritePartner(allPartnerStats.get(0));

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

    private List<PartnerStats> getPartnerStats(Player player, List<Match> matches) {
        Map<EphemeralTeam, EphemeralTeamStats> statsByTeam = new HashMap<>();

        for(Match match : matches) {
            EphemeralTeam team = match.playedForRed(player)
                    ? new EphemeralTeam(match.getRedTeamPlayer1(), match.getRedTeamPlayer2())
                    : new EphemeralTeam(match.getYellowTeamPlayer1(), match.getYellowTeamPlayer2());

            boolean won = match.isWonBy(player);

            if(statsByTeam.containsKey(team)) {
                statsByTeam.get(team).addResult(won);
            } else {
                statsByTeam.put(team, new EphemeralTeamStats(won));
            }
        }

        List<PartnerStats> allPartnerStats = new ArrayList<>();

        for(Map.Entry<EphemeralTeam, EphemeralTeamStats> statsEntry : statsByTeam.entrySet()) {
            allPartnerStats.add(new PartnerStats(statsEntry.getKey(), player, statsEntry.getValue()));
        }

        if(allPartnerStats.stream()
                .anyMatch(s -> s.getMatchesTogether() >= PARTNER_THRESHOLD)) {
            return allPartnerStats.stream()
                    .filter(s -> s.getMatchesTogether() >= PARTNER_THRESHOLD)
                    .collect(Collectors.toList());
        }

        return allPartnerStats;
    }

}
