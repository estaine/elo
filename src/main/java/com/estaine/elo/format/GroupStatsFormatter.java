package com.estaine.elo.format;

import com.estaine.elo.entity.nt.Box;
import com.estaine.elo.entity.tournament.Group;
import com.estaine.elo.entity.tournament.Team;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Component;

@Component
public class GroupStatsFormatter {

    public List<Group> formatGroupStats(Map<String, Group> groupsByName) {
        List<Group> groups = new ArrayList<>();

        for(Entry<String, Group> groupByName : groupsByName.entrySet()) {
            groups.add(groupByName.getValue());
        }

        groups.sort(Comparator.comparing(Group::getName));
        groups.forEach(g -> (g.getTeams()).sort(new GroupStandingComparator()));
        groups.forEach(g -> g.getGames().forEach(gg -> gg.setGroup(null)));
        return groups;
    }

    public List<Box> formatBoxStats(List<Box> boxes) {
        List<Box> sortedBoxes = new ArrayList<>();

        sortedBoxes.addAll(boxes);
        sortedBoxes.sort(Comparator.comparing(Box::getName));
        sortedBoxes.forEach(b -> (b.getTeams()).sort(new BoxStandingComparator()));
        sortedBoxes.forEach(b -> b.getBoxGames().forEach(bg -> bg.setBox(null)));

        return sortedBoxes;
    }



    public static class GroupStandingComparator implements Comparator<Team> {
        @Override
        public int compare(Team t1, Team t2) {
            if(t1.getPoints() == t2.getPoints()) {
                if(t1.getGoalsDelta() == t2.getGoalsDelta()) {
                    if(t1.getGamesPlayed() == t2.getGamesPlayed()) {
                        return t1.getName().compareTo(t2.getName());
                    }
                    return Integer.compare(t1.getGamesPlayed(), t2.getGamesPlayed());
                }

                return Integer.compare(t2.getGoalsDelta(), t1.getGoalsDelta());
            }

            return Integer.compare(t2.getPoints(), t1.getPoints());
        }
    }

    public static class BoxStandingComparator implements Comparator<com.estaine.elo.entity.nt.Team> {
        @Override
        public int compare(com.estaine.elo.entity.nt.Team t1, com.estaine.elo.entity.nt.Team t2) {
            if(t1.getPoints() == t2.getPoints()) {
                if(t1.getGoalsDelta() == t2.getGoalsDelta()) {
                    if(t1.getGamesPlayed() == t2.getGamesPlayed()) {
                        return t1.getName().compareTo(t2.getName());
                    }
                    return Integer.compare(t1.getGamesPlayed(), t2.getGamesPlayed());
                }

                return Integer.compare(t2.getGoalsDelta(), t1.getGoalsDelta());
            }

            return Integer.compare(t2.getPoints(), t1.getPoints());
        }
    }
}
