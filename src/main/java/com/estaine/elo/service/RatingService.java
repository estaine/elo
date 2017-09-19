package com.estaine.elo.service;

import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.PlayerStats;
import java.util.Map;

public interface RatingService {
    Map<Player, PlayerStats> calculateRatings();
}
