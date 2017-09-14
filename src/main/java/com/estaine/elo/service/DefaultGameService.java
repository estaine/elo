package com.estaine.elo.service;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.tournament.Group;
import com.estaine.elo.entity.tournament.GroupGame;
import com.estaine.elo.entity.tournament.Team;
import com.estaine.elo.repository.GameRepository;
import com.estaine.elo.repository.PlayerRepository;
import com.estaine.elo.repository.TournamentFileRepository;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultGameService implements GameService {

    private static final String ACCEPTED_CHANNEL_NAME = "general";
    private static final String TOKEN = "aQlauXOI7fAL5U3s62z6KnJA";

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TournamentFileRepository tournamentFileRepository;

    @Override
    public String registerMatch(String requester, String channelName, String request, String token) {
        if (!TOKEN.equals(token)) {
            return "Wrong token. Did you send your request not from Slack?";
        }

        if (!ACCEPTED_CHANNEL_NAME.equals(channelName)) {
            return "Matches can be only registered from channel #" + ACCEPTED_CHANNEL_NAME;
        }

        try {

            String[] requestParts = request.split(" ");

            String red1Username = requestParts[0];
            String red2Username = requestParts[1];
            String yellow1Username = requestParts[3];
            String yellow2Username = requestParts[4];

            Set<String> usernameSet = new HashSet<>(Arrays.asList(red1Username, red2Username, yellow1Username, yellow2Username));

            if (usernameSet.size() < 4) {
                return "All players in match should be unique";
            }

            Player red1 = playerRepository.findByUsername(red1Username);

            if (red1 == null) {
                return "No player with username " + red1Username + " was found in the DB";
            }

            Player red2 = playerRepository.findByUsername(red2Username);

            if (red2 == null) {
                return "No player with username " + red2Username + " was found in the DB";
            }

            Player yellow1 = playerRepository.findByUsername(yellow1Username);

            if (yellow1 == null) {
                return "No player with username " + yellow1Username + " was found in the DB";
            }

            Player yellow2 = playerRepository.findByUsername(yellow2Username);

            if (yellow2 == null) {
                return "No player with username " + yellow2Username + " was found in the DB";
            }

            String[] resultParts = requestParts[5].split(":");
            Integer redGoals = Integer.parseInt(resultParts[0]);
            Integer yellowGoals = Integer.parseInt(resultParts[1]);

            String requesterUsername = "@" + requester;

            if (Objects.equals(redGoals, yellowGoals)) {
                return "Draws are not supported";
            }

            boolean redLostRequesterNotRed = (redGoals < yellowGoals)
                    && (!requesterUsername.equals(red1Username) && !requesterUsername.equals(red2Username));

            boolean yellowLostButRequesterNotYellow = (yellowGoals < redGoals)
                    && (!requesterUsername.equals(yellow1Username) && !requesterUsername.equals(yellow2Username));

            if (redLostRequesterNotRed || yellowLostButRequesterNotYellow) {
                return "Only losers may register their matches";
            }

            Game game = new Game();
            game.setRedTeamPlayer1(red1);
            game.setRedTeamPlayer2(red2);
            game.setYellowTeamPlayer1(yellow1);
            game.setYellowTeamPlayer2(yellow2);
            game.setRedTeamGoals(redGoals);
            game.setYellowTeamGoals(yellowGoals);
            game.setPlayedOn(new Date());
            game.setTournamentGame(false);

            gameRepository.save(game);

            return "Match registered";
        } catch (Exception e) {
            return "Bad request format. Please format your command according to the example";
        }
    }

    @Override
    public String registerTournamentMatch(String requester, String channelName, String request, String token) {
        if (!TOKEN.equals(token)) {
            return "Wrong token. Did you send your request not from Slack?";
        }

        if (!ACCEPTED_CHANNEL_NAME.equals(channelName)) {
            return "Matches can be only registered from channel #" + ACCEPTED_CHANNEL_NAME;
        }

        try {
            String[] requestParts = request.split(" ");

            String red1Username = requestParts[0];
            String red2Username = requestParts[1];
            String yellow1Username = requestParts[3];
            String yellow2Username = requestParts[4];

            Set<String> usernameSet = new HashSet<>(Arrays.asList(red1Username, red2Username, yellow1Username, yellow2Username));

            if (usernameSet.size() < 4) {
                return "All players in match should be unique";
            }

            Set<Player> players = tournamentFileRepository.readParticipants();
            Map<String, Team> teams = tournamentFileRepository.readTeams();
            Map<String, Group> groups = tournamentFileRepository.readGroups(teams);
            Map<String, List<GroupGame>> groupGames = tournamentFileRepository.readGroupGames(groups, teams);

            if(players.stream().noneMatch(u -> u.getUsername().equals(red1Username))) {
                return "Player " + red1Username + " is not registered as a participant of current tournament";
            }
            Player red1 = players.stream().
                    filter(p -> p.getUsername().equals(red1Username)).findFirst().get();

            if(players.stream().noneMatch(u -> u.getUsername().equals(red2Username))) {
                return "Player " + red2Username + " is not registered as a participant of current tournament";
            }
            Player red2 = players.stream().
                    filter(p -> p.getUsername().equals(red2Username)).findFirst().get();

            if(players.stream().noneMatch(u -> u.getUsername().equals(yellow1Username))) {
                return "Player " + yellow1Username + " is not registered as a participant of current tournament";
            }
            Player yellow1 = players.stream().
                    filter(p -> p.getUsername().equals(yellow1Username)).findFirst().get();

            if(players.stream().noneMatch(u -> u.getUsername().equals(yellow2Username))) {
                return "Player " + yellow2Username + " is not registered as a participant of current tournament";
            }
            Player yellow2 = players.stream().
                    filter(p -> p.getUsername().equals(yellow2Username)).findFirst().get();

            Team redTeam = null, yellowTeam = null;

            for(Entry<String, Team> teamByName : teams.entrySet()) {

                if(teamByName.getValue().consistsOf(red1, red2)) {
                    redTeam = teamByName.getValue();
                }

                if(teamByName.getValue().consistsOf(yellow1, yellow2)) {
                    yellowTeam = teamByName.getValue();
                }
            }

            if(redTeam == null) {
                return "Players " + red1Username + " and " + red2Username + " are registered in different teams";
            }

            if(yellowTeam == null) {
                return "Players " + yellow1Username + " and " + yellow2Username + " are registered in different teams";
            }

            Group group = null;

            for(Entry<String, Group> groupByName : groups.entrySet()) {
                if(groupByName.getValue().contains(redTeam, yellowTeam)) {
                    group = groupByName.getValue();
                }
            }

            if(group == null) {
                return "The specified teams are playing in different groups";
            }
//TODO finish
            return null;


        } catch (Exception e) {
            return "Bad request format. Please format your command according to the example";
        }
    }
}
