package com.estaine.elo.entity;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class PlayerStats {
    private Player player;
    private List<Game> games;
    private Integer goalsFor;
    private Integer goalsAgainst;
    private BaseStats baseStats;
    private Map<Long, Boolean> results;
}
