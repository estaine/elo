package com.estaine.elo.service.impl;

import com.estaine.elo.entity.Match;
import com.estaine.elo.entity.tournament.Group;
import com.estaine.elo.entity.tournament.GroupMatch;
import com.estaine.elo.entity.tournament.PlayoffMatch;
import com.estaine.elo.entity.tournament.PlayoffSerie;
import com.estaine.elo.entity.tournament.Stage;
import com.estaine.elo.entity.tournament.Team;
import com.estaine.elo.entity.tournament.Tournament;
import com.estaine.elo.format.GroupStatsFormatter;
import com.estaine.elo.repository.GroupMatchRepository;
import com.estaine.elo.repository.PlayoffSerieRepository;
import com.estaine.elo.repository.TournamentRepository;
import com.estaine.elo.service.TournamentService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTournamentService implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final GroupMatchRepository groupMatchRepository;
    private final GroupStatsFormatter groupStatsFormatter;
    private final PlayoffSerieRepository playoffSerieRepository;

    @Autowired
    public DefaultTournamentService(@NonNull TournamentRepository tournamentRepository,
            @NonNull GroupMatchRepository groupMatchRepository,
            @NonNull GroupStatsFormatter groupStatsFormatter,
            @NonNull PlayoffSerieRepository playoffSerieRepository) {
        this.tournamentRepository = tournamentRepository;
        this.groupMatchRepository = groupMatchRepository;
        this.groupStatsFormatter = groupStatsFormatter;
        this.playoffSerieRepository = playoffSerieRepository;
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
    public void startPlayoffStage(Long tournamentId) {
        Tournament tournament = tournamentRepository.findOne(tournamentId);

        if (tournament == null) {
            return;
        }

        tournament = groupStatsFormatter.sortGroupStage(getGroupStats());

        List<Group> groups = tournament.getGroups();
        int groupCount = groups.size();

        if (!isPowOfTwo(groupCount)) {
            return;
        }

        List<PlayoffSerie> playoffSeries = new ArrayList<>();

        PlayoffSerie finalSerie = new PlayoffSerie(tournament, null, null, 3, Stage.FINAL);
        playoffSeries.add(finalSerie);

        PlayoffSerie thirdPlaceSerie = new PlayoffSerie(tournament, null, null, 2, Stage.THIRD_PLACE_MATCH);
        playoffSeries.add(thirdPlaceSerie);

        PlayoffSerie firstSemiFinalSerie = new PlayoffSerie(tournament, groups.get(0).getTeams().get(0),
                groups.get(1).getTeams().get(1), 2, Stage.SEMIFINAL, finalSerie, thirdPlaceSerie);
        playoffSeries.add(firstSemiFinalSerie);

        PlayoffSerie secondSemiFinalSerie = new PlayoffSerie(tournament, groups.get(1).getTeams().get(0),
                groups.get(0).getTeams().get(1), 2, Stage.SEMIFINAL, finalSerie, thirdPlaceSerie);
        playoffSeries.add(secondSemiFinalSerie);

        for (PlayoffSerie playoffSerie : playoffSeries) {
            for (int i = 0; i < playoffSerie.getBestOf(); i++) {
                if ((playoffSerie.getFirstTeam() != null) && (playoffSerie.getSecondTeam() != null)) {
                    Team redTeam = (i % 2 == 0) ? playoffSerie.getFirstTeam() : playoffSerie.getSecondTeam();
                    Team yellowTeam = (i % 2 == 0) ? playoffSerie.getSecondTeam() : playoffSerie.getFirstTeam();

                    playoffSerie.getPlayoffMatches().add(new PlayoffMatch(redTeam, yellowTeam, playoffSerie));
                }
            }
        }

        playoffSerieRepository.save(playoffSeries);
    }

    @Override
    public Tournament getGroupStats() {
        Tournament tournament = tournamentRepository.findByActiveTrue();

        if (tournament == null) {
            return null;
        }

        for (Group group : tournament.getGroups()) {
            for (GroupMatch groupMatch : group.getGroupMatches()) {
                if (groupMatch.isPlayed()) {
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

    @Override
    public Tournament getPlayoffStats() {
        Tournament tournament = tournamentRepository.findByActiveTrue();

        LinkedHashMap<Stage, List<PlayoffSerie>> seriesByStage = new LinkedHashMap();

        List<Stage> stages = Arrays.asList(Stage.values());
        Collections.reverse(stages);

        for (Stage stage : stages) {
            seriesByStage.put(stage, new ArrayList<>());
        }

        tournament.getPlayoffSeries().forEach(ps -> seriesByStage.get(ps.getStage()).add(ps));

        for (PlayoffSerie playoffSerie : tournament.getPlayoffSeries()) {
            for (PlayoffMatch playoffMatch : playoffSerie.getPlayoffMatches()) {
                if (playoffMatch.isPlayed()) {
                    Match match = playoffMatch.getMatch();

                    Team winner = (match.getRedTeamGoals() > match.getYellowTeamGoals())
                            ? playoffMatch.getRedTeam()
                            : playoffMatch.getYellowTeam();

                    if (winner.getId().equals(playoffSerie.getFirstTeam().getId())) {
                        playoffSerie.setFirstTeamWinCount(playoffSerie.getFirstTeamWinCount() + 1);
                    } else {
                        playoffSerie.setSecondTeamWinCount(playoffSerie.getFirstTeamWinCount() + 1);
                    }
                }
            }
        }

        tournament.setSeriesByStage(seriesByStage);

        return tournament;
    }

    private boolean isPowOfTwo(int n) {
        return (n != 0) && ((n & (n - 1)) == 0);
    }

}
