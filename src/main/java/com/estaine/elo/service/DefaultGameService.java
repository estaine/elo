package com.estaine.elo.service;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.tournament.Box;
import com.estaine.elo.entity.tournament.BoxGame;
import com.estaine.elo.entity.tournament.Team;
import com.estaine.elo.entity.tournament.Tournament;
import com.estaine.elo.repository.BoxGameRepository;
import com.estaine.elo.repository.GameRepository;
import com.estaine.elo.repository.PlayerRepository;
import com.estaine.elo.repository.TournamentRepository;
import com.estaine.elo.request.SlackNotifier;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultGameService implements GameService {

    private static final String ACCEPTED_CHANNEL_NAME = "by_kicker";

    @Value("${slack-token}")
    private String slackToken;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private BoxGameRepository boxGameRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private SlackNotifier slackNotifier;

    @Override
    public String registerMatch(String requesterUsername, String channelName, String request, String token) {
        if (!slackToken.equals(token)) {
            return "Wrong token. Did you send your request not from Slack?";
        }

        if (!ACCEPTED_CHANNEL_NAME.equals(channelName)) {
            return "Matches can be only registered from channel #" + ACCEPTED_CHANNEL_NAME;
        }

        try {

            String[] requestParts = request.split(" ");

            String red1SlackId = requestParts[0].replace("@", "");
            String red2SlackId = requestParts[1].replace("@", "");
            String yellow1SlackId = requestParts[3].replace("@", "");
            String yellow2SlackId = requestParts[4].replace("@", "");

            Set<String> slackIdSet = new HashSet<>(Arrays.asList(red1SlackId, red2SlackId, yellow1SlackId, yellow2SlackId));

            if (slackIdSet.size() < 4) {
                return "All players in match should be unique";
            }

            Player red1 = playerRepository.findBySlackId(red1SlackId);

            if (red1 == null) {
                red1 = playerRepository.findByUsername("@" + red1SlackId);

                if(red1 == null) {
                    return "No player with username " + red1SlackId + " was found in the DB";
                }
            }

            Player red2 = playerRepository.findBySlackId(red2SlackId);

            if (red2 == null) {
                red2 = playerRepository.findByUsername("@" + red2SlackId);

                if(red2 == null) {
                    return "No player with username " + red2SlackId + " was found in the DB";
                }
            }

            Player yellow1 = playerRepository.findBySlackId(yellow1SlackId);

            if (yellow1 == null) {
                yellow1 = playerRepository.findByUsername("@" + yellow1SlackId);

                if(yellow1 == null) {
                    return "No player with username " + yellow1SlackId + " was found in the DB";
                }
            }

            Player yellow2 = playerRepository.findBySlackId(yellow2SlackId);

            if (yellow2 == null) {
                yellow2 = playerRepository.findByUsername("@" + yellow2SlackId);

                if(yellow2 == null) {
                    return "No player with username " + yellow2SlackId + " was found in the DB";
                }
            }

            String[] resultParts = requestParts[5].split(":");
            Integer redGoals = Integer.parseInt(resultParts[0]);
            Integer yellowGoals = Integer.parseInt(resultParts[1]);

            Player requester = playerRepository.findByUsername("@" + requesterUsername);

            if (Objects.equals(redGoals, yellowGoals)) {
                return "Draws are not supported";
            }

            if ((redGoals > 10) || (yellowGoals > 10)) {
                return "Goals count should be no more than 10 for each team";
            }

            if ((redGoals < 0) || (yellowGoals < 0)) {
                return "Negative goals count? Really?..;";
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

            slackNotifier.notifyMatchParticipants(requester, game);

            return "Match registered";
        } catch (Exception e) {
            return "Bad request format. Please format your command according to the example";
        }
    }

    @Override
    public String registerGroupMatch(String requesterUsername, String channelName, String request, String token) {
        if (!slackToken.equals(token)) {
            return "Wrong token. Did you send your request not from Slack?";
        }

        if (!ACCEPTED_CHANNEL_NAME.equals(channelName)) {
            return "Matches can be only registered from channel #" + ACCEPTED_CHANNEL_NAME;
        }

        try {

            String[] requestParts = request.split(" ");

            String red1SlackId = requestParts[0].replace("@", "");
            String red2SlackId = requestParts[1].replace("@", "");
            String yellow1SlackId = requestParts[3].replace("@", "");
            String yellow2SlackId = requestParts[4].replace("@", "");

            Set<String> slackIdSet = new HashSet<>(Arrays.asList(red1SlackId, red2SlackId, yellow1SlackId, yellow2SlackId));

            if (slackIdSet.size() < 4) {
                return "All players in match should be unique";
            }

            Player red1 = playerRepository.findBySlackId(red1SlackId);

            if (red1 == null) {
                return "No player with username " + red1SlackId + " was found in the DB";
            }

            Player red2 = playerRepository.findBySlackId(red2SlackId);

            if (red2 == null) {
                return "No player with username " + red2SlackId + " was found in the DB";
            }

            Player yellow1 = playerRepository.findBySlackId(yellow1SlackId);

            if (yellow1 == null) {
                return "No player with username " + yellow1SlackId + " was found in the DB";
            }

            Player yellow2 = playerRepository.findBySlackId(yellow2SlackId);

            if (yellow2 == null) {
                return "No player with username " + yellow2SlackId + " was found in the DB";
            }

            String[] resultParts = requestParts[5].split(":");
            Integer redGoals = Integer.parseInt(resultParts[0]);
            Integer yellowGoals = Integer.parseInt(resultParts[1]);

            Player requester = playerRepository.findByUsername("@" + requesterUsername);

            if (Objects.equals(redGoals, yellowGoals)) {
                return "Draws are not supported";
            }

            if ((redGoals > 10) || (yellowGoals > 10)) {
                return "Goals count should be no more than 10 for each team";
            }

            if ((redGoals < 0) || (yellowGoals < 0)) {
                return "Negative goals count? Really?..;";
            }

            Tournament tournament = tournamentRepository.findByActiveTrue();

            Team redTeam = tournament.getTeams().stream().filter(t -> t.consistsOf(red1, red2)).findFirst().orElse(null);

            if(redTeam == null) {
                return "No team consisting of " + red1.getUsername() + " and " + red2.getUsername() + " found in current tournament";
            }

            Team yellowTeam = tournament.getTeams().stream().filter(t -> t.consistsOf(yellow1, yellow2)).findFirst().orElse(null);

            if(yellowTeam == null) {
                return "No team consisting of " + yellow1.getUsername() + " and " + yellow2.getUsername() + " found in current tournament";
            }

            if(!redTeam.getBox().getId().equals(yellowTeam.getBox().getId())) {
                return "The specified teams belong to different groups. Try to register a common match instead.";
            }

            Box box = redTeam.getBox();

            BoxGame boxGame = box.getBoxGames().stream()
                    .filter(bg -> bg.consistsOf(redTeam, yellowTeam))
                    .findFirst()
                    .orElse(null);

            if(boxGame == null) {
                return "No match is planned between the specified teams. Perhaps the tournament is not started yet?";
            }

            if(boxGame.isPlayed()) {
                return "The specified teams have already played a match";
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

            boxGame.setGame(game);

            boxGame = boxGameRepository.save(boxGame);

            slackNotifier.notifyGroupMatchParticipants(requester, boxGame);

            return "Group match registered";
        } catch (Exception e) {
            return "Bad request format. Please format your command according to the example";
        }
    }
}
