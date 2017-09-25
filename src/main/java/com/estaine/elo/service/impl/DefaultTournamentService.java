package com.estaine.elo.service.impl;

import com.estaine.elo.entity.tournament.Group;
import com.estaine.elo.entity.tournament.GroupMatch;
import com.estaine.elo.entity.tournament.Tournament;
import com.estaine.elo.repository.GroupMatchRepository;
import com.estaine.elo.repository.TournamentRepository;
import com.estaine.elo.service.TournamentService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTournamentService implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final GroupMatchRepository groupMatchRepository;

    @Autowired
    public DefaultTournamentService(@NonNull TournamentRepository tournamentRepository,
                                    @NonNull GroupMatchRepository groupMatchRepository) {
        this.tournamentRepository = tournamentRepository;
        this.groupMatchRepository = groupMatchRepository;
    }

    @Override
    public void startGroupStage(Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);

        if (tournament == null) {
            return;
        }

        List<GroupMatch> groupMatches = new ArrayList<>();

        for (Group group : tournament.getGroups()) {
            for (com.estaine.elo.entity.tournament.Team redTeam : group.getTeams()) {
                for (com.estaine.elo.entity.tournament.Team yellowTeam : group.getTeams()) {
                    if (!redTeam.getId().equals(yellowTeam.getId())) {
                        groupMatches.add(new GroupMatch(redTeam, yellowTeam, group));
                    }
                }
            }
        }

        Collections.shuffle(groupMatches);
        groupMatchRepository.save(groupMatches);

    }

    @Override
    public Tournament getGroupStats() {
        Tournament tournament = tournamentRepository.findByActiveTrue();

        if (tournament == null) {
            return null;
        }

        for(Group group : tournament.getGroups()) {
            for(GroupMatch groupMatch : group.getGroupMatches()) {
                if(groupMatch.isPlayed()) {
                    com.estaine.elo.entity.tournament.Team winner =
                            (groupMatch.getMatch().getRedTeamGoals() > groupMatch.getMatch().getYellowTeamGoals())
                            ? groupMatch.getRedTeam() : groupMatch.getYellowTeam();
                    com.estaine.elo.entity.tournament.Team loser = (winner == groupMatch.getRedTeam())
                            ? groupMatch.getYellowTeam() : groupMatch.getRedTeam();

                    int delta =
                            Math.abs(groupMatch.getMatch().getRedTeamGoals() - groupMatch.getMatch().getYellowTeamGoals());

                    winner.registerMatch(delta);
                    loser.registerMatch(-delta);
                }
            }
        }

        return tournament;
    }
}
