package com.estaine.elo.format;

import com.estaine.elo.entity.tournament.Group;
import com.estaine.elo.entity.tournament.GroupMatch;
import com.estaine.elo.entity.tournament.Tournament;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GroupStatsFormatter {

    public Tournament formatGroupStage(Tournament tournament) {
        sortGroupStage(tournament);

        List<Group> groups = tournament.getGroups();
        groups.forEach(b -> b.getGroupMatches().forEach(bg -> bg.setGroup(null)));
        groups.forEach(b -> b.getGroupMatches().stream()
                .filter(GroupMatch::isPlayed)
                .forEach(bg -> bg.getMatch().clearTournamentMatch()));

        return tournament;
    }

    public Tournament sortGroupStage(Tournament tournament) {
        List<Group> groups = tournament.getGroups();

        groups.sort(Comparator.comparing(Group::getName));
        groups.forEach(b -> (b.getTeams()).sort(new GroupStandingComparator()));

        return tournament;
    }


    public static class GroupStandingComparator implements Comparator<com.estaine.elo.entity.tournament.Team> {
        @Override
        public int compare(com.estaine.elo.entity.tournament.Team t1, com.estaine.elo.entity.tournament.Team t2) {
            if (t1.getPoints() == t2.getPoints()) {
                if (t1.getGoalsDelta() == t2.getGoalsDelta()) {
                    if (t1.getMatchesPlayed() == t2.getMatchesPlayed()) {
                        return t1.getName().compareTo(t2.getName());
                    }
                    return Integer.compare(t1.getMatchesPlayed(), t2.getMatchesPlayed());
                }

                return Integer.compare(t2.getGoalsDelta(), t1.getGoalsDelta());
            }

            return Integer.compare(t2.getPoints(), t1.getPoints());
        }
    }
}
