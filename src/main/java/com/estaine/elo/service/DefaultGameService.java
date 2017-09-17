package com.estaine.elo.service;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.tournament.Group;
import com.estaine.elo.entity.tournament.GroupGame;
import com.estaine.elo.entity.tournament.Team;
import com.estaine.elo.repository.GameRepository;
import com.estaine.elo.repository.PlayerRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.estaine.elo.request.SlackNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultGameService implements GameService {

    private static final String ACCEPTED_CHANNEL_NAME = "by_kicker";
    private static final String TOKEN = "WausWIrVX3IvJAgRowlMHWlp";

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SlackNotifier slackNotifier;

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

            if((redGoals > 10) || (yellowGoals > 10)) {
                return "Goals count should be no more than 10 for each team";
            }

            if((redGoals < 0) || (yellowGoals < 0)) {
                return "Negative goals count? Really?..;";
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

            game = gameRepository.save(game);

            slackNotifier.notifyMatchParticipants(playerRepository.findByUsername(requester), game);

            return "Match registered";
        } catch (Exception e) {
            return "Bad request format. Please format your command according to the example";
        }
    }

}
