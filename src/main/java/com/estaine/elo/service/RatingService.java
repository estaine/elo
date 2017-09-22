package com.estaine.elo.service;

import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.PlayerStats;

import java.time.LocalDateTime;
import java.util.Map;

public interface RatingService {

    Map<Player, PlayerStats> calculateRatings();
    Map<Player, PlayerStats> calculateRatings(LocalDateTime base);
    Map<Player, PlayerStats> calculateRatings(LocalDateTime base, LocalDateTime start);

}
