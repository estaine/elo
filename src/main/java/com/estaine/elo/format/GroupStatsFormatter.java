package com.estaine.elo.format;

import com.estaine.elo.entity.tournament.Box;
import com.estaine.elo.entity.tournament.BoxGame;
import com.estaine.elo.entity.tournament.Tournament;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class GroupStatsFormatter {

    public Tournament formatTournament(Tournament tournament) {
        List<Box> boxes = tournament.getBoxes();

        boxes.sort(Comparator.comparing(Box::getName));
        boxes.forEach(b -> (b.getTeams()).sort(new BoxStandingComparator()));
        boxes.forEach(b -> b.getBoxGames().forEach(bg -> bg.setBox(null)));
        boxes.forEach(b -> b.getBoxGames().stream()
                .filter(BoxGame::isPlayed)
                .forEach(bg -> bg.getGame().setBoxGame(null)));

        return tournament;
    }


    public static class BoxStandingComparator implements Comparator<com.estaine.elo.entity.tournament.Team> {
        @Override
        public int compare(com.estaine.elo.entity.tournament.Team t1, com.estaine.elo.entity.tournament.Team t2) {
            if (t1.getPoints() == t2.getPoints()) {
                if (t1.getGoalsDelta() == t2.getGoalsDelta()) {
                    if (t1.getGamesPlayed() == t2.getGamesPlayed()) {
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
