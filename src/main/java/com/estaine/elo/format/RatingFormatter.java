package com.estaine.elo.format;

import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.BaseStats;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RatingFormatter {

    private static final int SIGNIFICANCE_THRESHOLD = 1;

    public List<Entry<Player, BaseStats>> formatRating(Map<Player, BaseStats> ratings) {
        List<Entry<Player, BaseStats>> sortedRatings = new LinkedList<>(ratings.entrySet());
        sortedRatings.sort(Comparator.comparing(stats -> stats.getValue().getRating()));
        Collections.reverse(sortedRatings);

        return sortedRatings.stream()
                .filter(e -> e.getValue().getGamesPlayed() >= SIGNIFICANCE_THRESHOLD)
                .collect(Collectors.toList());

    }

}
