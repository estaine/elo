package com.estaine.elo.service;

import com.estaine.elo.dto.PlayerStats;

public interface PlayerStatsService {

    PlayerStats getPlayerStats(String username);

}
