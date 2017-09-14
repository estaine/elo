package com.estaine.elo.repository;

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
import java.util.Set;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class TournamentFileRepository {

    private static final ObjectMapper JSON = new ObjectMapper();

    @Autowired
    private ResourceLoader resourceLoader;

    @SneakyThrows
    public Set<Player> readParticipants() {
        return JSON.readValue(resourceLoader.getResource("classpath:/static/tournaments/sep-17/participants.json").getInputStream(),
                new TypeReference<Set<Player>>() {
                });
    }

    @SneakyThrows
    public Map<String, Team> readTeams() {
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
    public Map<String, Group> readGroups(Map<String, Team> teams) {
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
    public Map<String, List<GroupGame>> readGroupGames(Map<String, Group> groups, Map<String, Team> teams) {
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
