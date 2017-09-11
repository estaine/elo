package com.estaine.elo.service;

import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.Stats;
import java.util.List;
import java.util.Map;

public interface RatingService {
    Map<Player, Stats> calculateRatings();
}
