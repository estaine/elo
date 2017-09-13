package com.estaine.elo.service;

import com.estaine.elo.entity.tournament.Group;
import com.estaine.elo.entity.tournament.GroupGame;
import com.estaine.elo.entity.tournament.Team;
import com.estaine.elo.repository.TournamentRepository;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTournamentService implements TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Override
    public Map<String, Group> getTournamentRoundRobinStats() {
        //Set<Player> players = readParticipants();
        Map<String, Team> teams = tournamentRepository.readTeams();
        Map<String, Group> groups = tournamentRepository.readGroups(teams);
        Map<String, List<GroupGame>> groupGames = tournamentRepository.readGroupGames(groups, teams);

        for (Entry<String, List<GroupGame>> gamesByGroup : groupGames.entrySet()) {
            for (GroupGame groupGame : gamesByGroup.getValue()) {
                Team winner = (groupGame.getRedGoals() > groupGame.getYellowGoals())
                        ? groupGame.getRedTeam() : groupGame.getYellowTeam();
                Team loser = (winner == groupGame.getRedTeam())
                        ? groupGame.getYellowTeam() : groupGame.getRedTeam();

                int delta = Math.abs(groupGame.getRedGoals() - groupGame.getYellowGoals());

                winner.registerGame(delta);
                loser.registerGame(-delta);
            }

            groups.get(gamesByGroup.getKey()).setGames(gamesByGroup.getValue());
        }
        return groups;
    }

    @Override
    public List<Group> getTournamentPlayoffStats() {
        return null;
    }
}
