package com.estaine.elo.format;

import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.PlayerStats;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RatingFormatter {

    @Value("${significance.threshold}")
    private int significanceThreshold;

    public List<PlayerStats> formatRating(Map<Player, PlayerStats> playerStatsMap) {
        List<PlayerStats> sortedRatings = new LinkedList<>(playerStatsMap.values());
        sortedRatings.sort(Comparator.comparing(stats -> stats.getBaseStats().getRating()));
        Collections.reverse(sortedRatings);

        sortedRatings = sortedRatings.stream()
                .filter(r -> r.getBaseStats().getGamesPlayed() >= significanceThreshold)
                .collect(Collectors.toList());

        sortedRatings.forEach(r -> r.getGames()
                .forEach(g -> g.setBoxGame(null)));

        return sortedRatings;

    }

}
