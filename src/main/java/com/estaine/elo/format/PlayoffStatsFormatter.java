package com.estaine.elo.format;

import com.estaine.elo.entity.tournament.PlayoffSerie;
import com.estaine.elo.entity.tournament.Stage;
import com.estaine.elo.entity.tournament.Tournament;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Component;

@Component
public class PlayoffStatsFormatter {

    public Tournament formatPlayoffStats(Tournament tournament) {
        tournament.getPlayoffSeries().sort(new PlayoffSerieComparator());

        //.forEach(e -> e.getValue().forEach(ps -> ps.getPlayoffMatches().forEach(pm -> pm.setPlayoffSerie(null))));
        //formattedStats.forEach(e -> e.getValue().forEach(ps -> ps.getPlayoffMatches().forEach(pm -> pm.setPlayoffSerie(null))));

        return tournament;
    }

    public static class PlayoffSerieComparator implements Comparator<PlayoffSerie> {
        @Override
        public int compare(PlayoffSerie s1, PlayoffSerie s2) {
            return Integer.compare(s1.getStage().getValue(), s2.getStage().getValue());
        }

    }
}
