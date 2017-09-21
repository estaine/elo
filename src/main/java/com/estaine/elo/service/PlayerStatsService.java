package com.estaine.elo.service;

import com.estaine.elo.entity.PlayerStats;

public interface PlayerStatsService {

    PlayerStats getPlayerStats(String username);

}
