package com.estaine.elo.service.impl;

import com.estaine.elo.entity.Player;
import com.estaine.elo.dto.PlayerStats;
import com.estaine.elo.repository.PlayerRepository;
import com.estaine.elo.service.PlayerStatsService;
import com.estaine.elo.service.RatingService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPlayerStatsService implements PlayerStatsService {

    private final PlayerRepository playerRepository;
    private final RatingService ratingService;

    @Autowired
    public DefaultPlayerStatsService(@NonNull PlayerRepository playerRepository, @NonNull RatingService ratingService) {
        this.playerRepository = playerRepository;
        this.ratingService = ratingService;
    }

    @Override
    public PlayerStats getPlayerStats(String username) {
        Player player = playerRepository.findByUsername(username);
        return ratingService.calculateRatings().get(player);
    }
}
