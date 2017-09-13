package com.estaine.elo.entity.tournament;

import java.util.List;
import lombok.Data;

@Data
public class Group {
    private String name;
    private List<Team> teams;
    private List<GroupGame> games;

    public boolean contains(Team team1, Team team2) {
        return teams.stream().anyMatch(t -> t.getName().equals(team1.getName()))
                && teams.stream().anyMatch(t -> t.getName().equals(team2.getName()));
    }
}
