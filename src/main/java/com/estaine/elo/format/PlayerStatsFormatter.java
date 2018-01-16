package com.estaine.elo.format;

import com.estaine.elo.dto.PlayerStats;
import com.estaine.elo.entity.Award;
import com.estaine.elo.entity.Match;
import java.util.Collections;
import java.util.Comparator;
import org.springframework.stereotype.Component;

@Component
public class PlayerStatsFormatter {

    public PlayerStats formatPlayerStats(PlayerStats playerStats) {
        playerStats.getMatches().forEach(Match::clearTournamentMatch);
        Collections.reverse(playerStats.getMatches());
        playerStats.getPlayer().getAwards().sort(new AwardComparator());
        playerStats.getPlayer().getAwards().forEach(a -> a.setPlayer(null));
        return playerStats;
    }

    public static class AwardComparator implements Comparator<Award> {
        @Override
        public int compare(Award a1, Award a2) {
            if(a1.getType() == a2.getType()) {
                if(a1.getLevel() == a2.getLevel()) {
                    return Integer.compare(a1.getId().intValue(), a2.getId().intValue());
                }
                else {
                    return Integer.compare(a1.getLevel().ordinal(), a2.getLevel().ordinal());
                }
            } else {
                return Integer.compare(a1.getType().ordinal(), a2.getType().ordinal());
            }
        }
    }
}
