package com.estaine.elo.service.impl;

import com.estaine.elo.entity.Match;
import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.tournament.Group;
import com.estaine.elo.entity.tournament.GroupMatch;
import com.estaine.elo.entity.tournament.Team;
import com.estaine.elo.entity.tournament.Tournament;
import com.estaine.elo.properties.SlackProperties;
import com.estaine.elo.repository.GroupMatchRepository;
import com.estaine.elo.repository.MatchRepository;
import com.estaine.elo.repository.PlayerRepository;
import com.estaine.elo.repository.TournamentRepository;
import com.estaine.elo.request.SlackNotifier;
import com.estaine.elo.service.MatchService;
import com.estaine.elo.service.exception.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class DefaultMatchService implements MatchService {

    private static final String COMMON_ERROR_MESSAGE = "Something went wrong. Please pray to Allah and try again.";

    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final GroupMatchRepository groupMatchRepository;
    private final TournamentRepository tournamentRepository;
    private final SlackNotifier slackNotifier;

    /**
     * @deprecated Must be moved to the controller
     */
    @Deprecated
    private final SlackProperties slackProperties;

    @Autowired
    public DefaultMatchService(@NonNull PlayerRepository playerRepository,
                              @NonNull MatchRepository matchRepository,
                              @NonNull GroupMatchRepository groupMatchRepository,
                              @NonNull TournamentRepository tournamentRepository,
                              @NonNull SlackNotifier slackNotifier,
                              @NonNull SlackProperties slackProperties) {
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.groupMatchRepository = groupMatchRepository;
        this.tournamentRepository = tournamentRepository;
        this.slackNotifier = slackNotifier;
        this.slackProperties = slackProperties;
    }

    @Override
    public String registerMatch(String requesterUsername, String channelName, String request, String token) {
        try {
            String slackFormattedRequesterUsername = "@" + requesterUsername;
            Match match = matchRepository.save(buildMatch(channelName, request, token, slackFormattedRequesterUsername));

            Player requester = playerRepository.findByUsername(slackFormattedRequesterUsername);
            slackNotifier.sendCommonMatchNotifications(requester, match);

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
            Match match = buildMatch(channelName, request, token, slackFormattedRequesterUsername);
            GroupMatch groupMatch = buildGroupMatch(match);

            matchRepository.save(match);
            groupMatch = groupMatchRepository.save(groupMatch);

            Player requester = playerRepository.findByUsername(slackFormattedRequesterUsername);
            slackNotifier.sendGroupMatchNotifications(requester, groupMatch);

            return "Group match registered";
        } catch (SlackRequestValidationException e) {
            return e.getMessage();
        } catch (Exception e) {
            return COMMON_ERROR_MESSAGE;
        }

    }

    private Match buildMatch(String channelName, String request, String token, String requesterUsername) throws SlackRequestValidationException {
        if (!slackProperties.getSecretKey().equals(token)) {
            throw new InvalidTokenException();
        }

        if (!slackProperties.getChannelName().equals(channelName)) {
            throw new InvalidChannelException(slackProperties.getChannelName());
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

        Match match = new Match();
        match.setRedTeamPlayer1(red1);
        match.setRedTeamPlayer2(red2);
        match.setYellowTeamPlayer1(yellow1);
        match.setYellowTeamPlayer2(yellow2);
        match.setRedTeamGoals(redGoals);
        match.setYellowTeamGoals(yellowGoals);
        match.setPlayedOn(LocalDateTime.now());
        match.setReportedBy(requesterUsername);

        return match;
    }

    private GroupMatch buildGroupMatch(Match match) throws SlackRequestValidationException {
        Tournament tournament = tournamentRepository.findByActiveTrue();

        Team redTeam = buildTeam(tournament, match.getRedTeamPlayer1(), match.getRedTeamPlayer2());
        Team yellowTeam = buildTeam(tournament, match.getYellowTeamPlayer1(), match.getYellowTeamPlayer2());

        if (!redTeam.getGroup().getId().equals(yellowTeam.getGroup().getId())) {
            throw new IncompatibleTeamsException();
        }

        Group group = redTeam.getGroup();

        GroupMatch groupMatch = group.getGroupMatches().stream()
                .filter(bg -> bg.consistsOf(redTeam, yellowTeam))
                .findFirst()
                .orElse(null);

        if (groupMatch == null) {
            throw new NoMatchScheduledException();
        }

        if (groupMatch.isPlayed()) {
            throw new MatchAlreadyPlayedException();
        }

        groupMatch.setMatch(match);

        return groupMatch;
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
