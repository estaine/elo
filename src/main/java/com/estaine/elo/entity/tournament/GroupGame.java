package com.estaine.elo.entity.tournament;

import lombok.Data;

@Data
public class GroupGame {
    private Group group;
    private Team redTeam;
    private Team yellowTeam;
    private int redGoals;
    private int yellowGoals;
}
