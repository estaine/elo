package com.estaine.elo.format;

import com.estaine.elo.dto.PlayerStats;
import java.util.Collections;
import org.springframework.stereotype.Component;

@Component
public class PlayerStatsFormatter {

    public PlayerStats formatPlayerStats(PlayerStats playerStats) {
        playerStats.getMatches().forEach(g -> g.setGroupMatch(null));
        Collections.reverse(playerStats.getMatches());
        return playerStats;
    }
}
