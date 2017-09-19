package com.estaine.elo.service;

import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.PlayerStats;
import com.estaine.elo.repository.GameRepository;
import com.estaine.elo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultPlayerStatsService implements PlayerStatsService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    RatingService ratingService;

    @Override
    public PlayerStats getPlayerStats(String username) {
        Player player = playerRepository.findByUsername(username);
        return ratingService.calculateRatings().get(player);
    }
}
