package com.estaine.elo.service;

import com.estaine.elo.entity.tournament.Tournament;
import com.estaine.elo.entity.tournament.Group;

public interface TournamentService {
    void startGroupStage(Long tournamentId);
    Tournament getBoxStats(Long tournamentId);
}
