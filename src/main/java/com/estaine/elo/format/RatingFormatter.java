package com.estaine.elo.format;

import com.estaine.elo.entity.Player;
import com.estaine.elo.dto.PlayerStats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RatingFormatter {

    @Value("${significance.threshold}")
    private int significanceThreshold;

    public List<PlayerStats> formatRating(Map<Player, PlayerStats> playerStatsMap) {
        return formatRating(playerStatsMap, this.significanceThreshold);
    }

    public List<PlayerStats> formatRating(Map<Player, PlayerStats> playerStatsMap, int significanceThreshold) {
        List<PlayerStats> sortedRatings = new LinkedList<>(playerStatsMap.values());
        sortedRatings.sort(Comparator.comparing(stats -> stats.getBaseStats().getRating()));
        Collections.reverse(sortedRatings);

        sortedRatings = sortedRatings.stream()
                .filter(r -> r.getBaseStats().getMatchesPlayed() >= significanceThreshold)
                .collect(Collectors.toList());

        sortedRatings.forEach(r -> r.getMatches()
                .forEach(g -> g.setGroupMatch(null)));

        return sortedRatings;
    }

}
