package com.estaine.elo.format;

import com.estaine.elo.entity.PlayerStats;
import java.util.Collections;
import org.springframework.stereotype.Component;

@Component
public class PlayerStatsFormatter {

    public PlayerStats formatPlayerStats(PlayerStats playerStats) {
        playerStats.getGames().forEach(g -> g.setBoxGame(null));
        Collections.reverse(playerStats.getGames());
        return playerStats;
    }
}
