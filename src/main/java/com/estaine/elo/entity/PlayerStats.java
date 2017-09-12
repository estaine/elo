package com.estaine.elo.entity;

import java.util.List;
import lombok.Data;

@Data
public class PlayerStats {
    private Player player;
    private List<Game> games;
    private Integer goalsFor;
    private Integer goalsAgainst;
    private BaseStats baseStats;
}
