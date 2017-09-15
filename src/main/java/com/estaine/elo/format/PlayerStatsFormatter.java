package com.estaine.elo.format;

import com.estaine.elo.entity.PlayerStats;
import org.springframework.stereotype.Component;

@Component
public class PlayerStatsFormatter {

    public PlayerStats formatPlayerStats(PlayerStats playerStats) {
        playerStats.getGames().forEach(g -> g.setBoxGame(null));
        return playerStats;
    }
}
