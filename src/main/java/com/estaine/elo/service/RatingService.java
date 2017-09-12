package com.estaine.elo.service;

import com.estaine.elo.entity.BaseStats;
import com.estaine.elo.entity.Player;
import java.util.Map;

public interface RatingService {
    Map<Player, BaseStats> calculateRatings();
}
