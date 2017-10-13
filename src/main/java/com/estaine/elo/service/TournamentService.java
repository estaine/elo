package com.estaine.elo.service;

import com.estaine.elo.entity.tournament.PlayoffSerie;
import com.estaine.elo.entity.tournament.Stage;
import com.estaine.elo.entity.tournament.Tournament;
import java.util.List;
import java.util.Map;

public interface TournamentService {

    void startGroupStage(Long tournamentId);
    void startPlayoffStage(Long tournamentId);

    Tournament getGroupStats();
    Tournament getPlayoffStats();

}
