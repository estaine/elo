package com.estaine.elo.service;

import com.estaine.elo.entity.nt.Box;
import com.estaine.elo.entity.tournament.Group;
import java.util.List;
import java.util.Map;

public interface TournamentService {
    Map<String, Group> getTournamentRoundRobinStats();
    List<Group> getTournamentPlayoffStats();
    void startGroupStage(Long tournamentId);
    List<Box> getBoxStats(Long tournamentId);
}
