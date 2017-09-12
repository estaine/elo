package com.estaine.elo.service;

import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.tournament.Group;
import com.estaine.elo.entity.tournament.GroupGame;
import com.estaine.elo.entity.tournament.Team;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class DefaultTournamentService implements TournamentService {

    private static final ObjectMapper JSON = new ObjectMapper();

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public Map<String, Group> getTournamentRoundRobinStats() {
        //Set<Player> players = readParticipants();
        Map<String, Team> teams = readTeams();
        Map<String, Group> groups = readGroups(teams);
        Map<String, List<GroupGame>> groupGames = readGroupGames(groups, teams);

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

    @SneakyThrows
    private Set<Player> readParticipants() {
        return JSON.readValue(resourceLoader.getResource("classpath:/static/tournaments/sep-17/participants.json").getInputStream(),
                new TypeReference<Set<Player>>() {
                });
    }

    @SneakyThrows
    private Map<String, Team> readTeams() {
        Set<Team> teams = JSON.readValue(resourceLoader.getResource("classpath:/static/tournaments/sep-17/teams.json").getInputStream(),
                new TypeReference<Set<Team>>() {
                });

        Map<String, Team> teamMap = new HashMap<>();

        for (Team team : teams) {
            teamMap.put(team.getName(), team);
        }

        return teamMap;
    }

    @SneakyThrows
    private Map<String, Group> readGroups(Map<String, Team> teams) {
        List<Group> groups = JSON.readValue(resourceLoader.getResource("classpath:/static/tournaments/sep-17/groups.json").getInputStream(),
                new TypeReference<List<Group>>() {
                });

        Map<String, Group> groupMap = new HashMap<>();

        for (Group group : groups) {
            List<Team> persistedTeams = new ArrayList<>();

            for (Team team : group.getTeams()) {
                persistedTeams.add(teams.get(team.getName()));
            }

            group.setTeams(persistedTeams);

            groupMap.put(group.getName(), group);
        }

        return groupMap;
    }

    @SneakyThrows
    private Map<String, List<GroupGame>> readGroupGames(Map<String, Group> groups, Map<String, Team> teams) {
        List<GroupGame> groupGames = JSON.readValue(resourceLoader.getResource("classpath:/static/tournaments/sep-17/group-games.json").getInputStream(),
                new TypeReference<List<GroupGame>>() {
                });

        Map<String, List<GroupGame>> groupGameMap = new HashMap<>();
        groups.forEach((name, group) -> groupGameMap.put(name, new ArrayList<>()));

        for (GroupGame groupGame : groupGames) {
            String groupName = groupGame.getGroup().getName();
            groupGame.setGroup(groups.get(groupName));
            groupGame.setRedTeam(teams.get(groupGame.getRedTeam().getName()));
            groupGame.setYellowTeam(teams.get(groupGame.getYellowTeam().getName()));
            groupGameMap.get(groupName).add(groupGame);
        }

        return groupGameMap;
    }
}
