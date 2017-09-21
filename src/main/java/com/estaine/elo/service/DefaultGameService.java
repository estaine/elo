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
import com.estaine.elo.service.exception.BadRequestFormatException;
import com.estaine.elo.service.exception.DrawResultException;
import com.estaine.elo.service.exception.DuplicatingPlayerException;
import com.estaine.elo.service.exception.IncompatibleTeamsException;
import com.estaine.elo.service.exception.InvalidChannelException;
import com.estaine.elo.service.exception.InvalidTokenException;
import com.estaine.elo.service.exception.MatchAlreadyPlayedException;
import com.estaine.elo.service.exception.MaxGoalsLimitExceededException;
import com.estaine.elo.service.exception.NegativeGoalsCountException;
import com.estaine.elo.service.exception.NoMatchScheduledException;
import com.estaine.elo.service.exception.PlayerNotFoundException;
import com.estaine.elo.service.exception.SlackRequestValidationException;
import com.estaine.elo.service.exception.TeamNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultGameService implements GameService {

    private static final String ACCEPTED_CHANNEL_NAME = "by_kicker";
    private static final String COMMON_ERROR_MESSAGE = "Something went wrong. Please pray to Allah and try again.";

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
        try {
            String slackFormattedRequesterUsername = "@" + requesterUsername;
            Game game = gameRepository.save(buildGame(channelName, request, token, slackFormattedRequesterUsername));

            Player requester = playerRepository.findByUsername(slackFormattedRequesterUsername);
            slackNotifier.notifyMatchParticipants(requester, game);

            return "Match registered";
        } catch (SlackRequestValidationException e) {
            return e.getMessage();
        } catch (Exception e) {
            return COMMON_ERROR_MESSAGE;
        }
    }

    @Override
    public String registerGroupMatch(String requesterUsername, String channelName, String request, String token) {
        try {
            String slackFormattedRequesterUsername = "@" + requesterUsername;
            Game game = buildGame(channelName, request, token, slackFormattedRequesterUsername);
            BoxGame boxGame = buildGroupGame(game);

            gameRepository.save(game);
            boxGame = boxGameRepository.save(boxGame);

            Player requester = playerRepository.findByUsername(slackFormattedRequesterUsername);
            slackNotifier.notifyGroupMatchParticipants(requester, boxGame);

            return "Group match registered";
        } catch (SlackRequestValidationException e) {
            return e.getMessage();
        } catch (Exception e) {
            return COMMON_ERROR_MESSAGE;
        }

    }

    private Game buildGame(String channelName, String request, String token, String requesterUsername) throws SlackRequestValidationException {
        if (!slackToken.equals(token)) {
            throw new InvalidTokenException();
        }

        if (!ACCEPTED_CHANNEL_NAME.equals(channelName)) {
            throw new InvalidChannelException(ACCEPTED_CHANNEL_NAME);
        }

        String[] requestParts = request.split(" ");

        if (requestParts.length != 6) {
            throw new BadRequestFormatException();
        }

        String red1Name = requestParts[0];
        String red2Name = requestParts[1];
        String yellow1Name = requestParts[3];
        String yellow2Name = requestParts[4];

        Set<String> nameSet = new HashSet<>(Arrays.asList(red1Name, red2Name, yellow1Name, yellow2Name));

        if (nameSet.size() < 4) {
            throw new DuplicatingPlayerException();
        }

        Player red1 = findPlayerInDB(red1Name);
        Player red2 = findPlayerInDB(red2Name);
        Player yellow1 = findPlayerInDB(yellow1Name);
        Player yellow2 = findPlayerInDB(yellow2Name);
        String[] resultParts = requestParts[5].split(":");

        if (resultParts.length != 2) {
            throw new BadRequestFormatException();
        }

        Integer redGoals, yellowGoals;

        try {
            redGoals = Integer.parseInt(resultParts[0]);
            yellowGoals = Integer.parseInt(resultParts[1]);
        } catch (NumberFormatException e) {
            throw new BadRequestFormatException();
        }

        if (Objects.equals(redGoals, yellowGoals)) {
            throw new DrawResultException();
        }

        if ((redGoals > 10) || (yellowGoals > 10)) {
            throw new MaxGoalsLimitExceededException();
        }

        if ((redGoals < 0) || (yellowGoals < 0)) {
            throw new NegativeGoalsCountException();
        }

        Game game = new Game();
        game.setRedTeamPlayer1(red1);
        game.setRedTeamPlayer2(red2);
        game.setYellowTeamPlayer1(yellow1);
        game.setYellowTeamPlayer2(yellow2);
        game.setRedTeamGoals(redGoals);
        game.setYellowTeamGoals(yellowGoals);
        game.setPlayedOn(LocalDateTime.now());
        game.setReportedBy(requesterUsername);

        return game;
    }

    private BoxGame buildGroupGame(Game game) throws SlackRequestValidationException {
        Tournament tournament = tournamentRepository.findByActiveTrue();

        Team redTeam = buildTeam(tournament, game.getRedTeamPlayer1(), game.getRedTeamPlayer2());
        Team yellowTeam = buildTeam(tournament, game.getYellowTeamPlayer1(), game.getYellowTeamPlayer2());

        if (!redTeam.getBox().getId().equals(yellowTeam.getBox().getId())) {
            throw new IncompatibleTeamsException();
        }

        Box box = redTeam.getBox();

        BoxGame boxGame = box.getBoxGames().stream()
                .filter(bg -> bg.consistsOf(redTeam, yellowTeam))
                .findFirst()
                .orElse(null);

        if (boxGame == null) {
            throw new NoMatchScheduledException();
        }

        if (boxGame.isPlayed()) {
            throw new MatchAlreadyPlayedException();
        }

        boxGame.setGame(game);

        return boxGame;
    }

    private Player findPlayerInDB(String username) throws PlayerNotFoundException {
        Player player = playerRepository.findByUsername(username);

        if (player == null) {
            player = playerRepository.findBySlackId(username.replace("@", ""));

            if (player == null) {
                throw new PlayerNotFoundException(username);
            }
        }

        return player;
    }

    private Team buildTeam(Tournament tournament, Player player1, Player player2) throws TeamNotFoundException {
        return tournament.getTeams().stream()
                .filter(t -> t.consistsOf(player1, player2))
                .findFirst()
                .orElseThrow(() -> new TeamNotFoundException(player1, player2));
    }
}
