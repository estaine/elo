package com.estaine.elo.service;

import com.estaine.elo.entity.nt.Box;
import com.estaine.elo.entity.nt.BoxGame;
import com.estaine.elo.entity.nt.Tournament;
import com.estaine.elo.entity.tournament.Group;
import com.estaine.elo.entity.tournament.GroupGame;
import com.estaine.elo.entity.tournament.Team;
import com.estaine.elo.repository.BoxGameRepository;
import com.estaine.elo.repository.TournamentFileRepository;
import com.estaine.elo.repository.TournamentRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTournamentService implements TournamentService {

    @Autowired
    private TournamentFileRepository tournamentFileRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private BoxGameRepository boxGameRepository;

    @Override
    public Map<String, Group> getTournamentRoundRobinStats() {
        //Set<Player> players = readParticipants();
        Map<String, Team> teams = tournamentFileRepository.readTeams();
        Map<String, Group> groups = tournamentFileRepository.readGroups(teams);
        Map<String, List<GroupGame>> groupGames = tournamentFileRepository.readGroupGames(groups, teams);

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

    @Override
    public void startGroupStage(Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);

        if (tournament == null) {
            return;
        }

        List<BoxGame> games = new ArrayList<>();

        for (Box box : tournament.getBoxes()) {
            for (com.estaine.elo.entity.nt.Team redTeam : box.getTeams()) {
                for (com.estaine.elo.entity.nt.Team yellowTeam : box.getTeams()) {
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
    public Tournament getBoxStats(Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);

        if (tournament == null) {
            return null;
        }

        for(Box box : tournament.getBoxes()) {
            for(BoxGame boxGame : box.getBoxGames()) {
                if(boxGame.isPlayed()) {
                    com.estaine.elo.entity.nt.Team winner =
                            (boxGame.getGame().getRedTeamGoals() > boxGame.getGame().getYellowTeamGoals())
                            ? boxGame.getRedTeam() : boxGame.getYellowTeam();
                    com.estaine.elo.entity.nt.Team loser = (winner == boxGame.getRedTeam())
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
