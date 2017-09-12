package com.estaine.elo.entity.tournament;

import java.util.List;
import lombok.Data;

@Data
public class Group {
    private String name;
    private List<Team> teams;
    private List<GroupGame> games;
}
