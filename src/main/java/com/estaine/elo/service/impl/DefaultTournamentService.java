package com.estaine.elo.service.impl;

import com.estaine.elo.entity.tournament.Box;
import com.estaine.elo.entity.tournament.BoxGame;
import com.estaine.elo.entity.tournament.Tournament;
import com.estaine.elo.repository.BoxGameRepository;
import com.estaine.elo.repository.TournamentRepository;
import com.estaine.elo.service.TournamentService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DefaultTournamentService implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final BoxGameRepository boxGameRepository;

    @Autowired
    public DefaultTournamentService(@NonNull TournamentRepository tournamentRepository,
                                    @NonNull BoxGameRepository boxGameRepository) {
        this.tournamentRepository = tournamentRepository;
        this.boxGameRepository = boxGameRepository;
    }

    @Override
    public void startGroupStage(Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);

        if (tournament == null) {
            return;
        }

        List<BoxGame> games = new ArrayList<>();

        for (Box box : tournament.getBoxes()) {
            for (com.estaine.elo.entity.tournament.Team redTeam : box.getTeams()) {
                for (com.estaine.elo.entity.tournament.Team yellowTeam : box.getTeams()) {
                    if (!redTeam.getId().equals(yellowTeam.getId())) {
                        games.add(new BoxGame(redTeam, yellowTeam, box));
                    }
                }
            }
        }

        Collections.shuffle(games);
        boxGameRepository.save(games);

    }

    @Override
    public Tournament getBoxStats() {
        Tournament tournament = tournamentRepository.findByActiveTrue();

        if (tournament == null) {
            return null;
        }

        for(Box box : tournament.getBoxes()) {
            for(BoxGame boxGame : box.getBoxGames()) {
                if(boxGame.isPlayed()) {
                    com.estaine.elo.entity.tournament.Team winner =
                            (boxGame.getGame().getRedTeamGoals() > boxGame.getGame().getYellowTeamGoals())
                            ? boxGame.getRedTeam() : boxGame.getYellowTeam();
                    com.estaine.elo.entity.tournament.Team loser = (winner == boxGame.getRedTeam())
                            ? boxGame.getYellowTeam() : boxGame.getRedTeam();

                    int delta =
                            Math.abs(boxGame.getGame().getRedTeamGoals() - boxGame.getGame().getYellowTeamGoals());

                    winner.registerGame(delta);
                    loser.registerGame(-delta);
                }
            }
        }

        return tournament;
    }
}