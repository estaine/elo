package com.estaine.elo.dto;

import com.estaine.elo.entity.Player;
import java.util.Comparator;
import lombok.Data;

@Data
public class PartnerStats {
    private Player partner;
    private Integer winsTogether;
    private Integer lossesTogether;

    public PartnerStats(EphemeralTeam team, Player targetPlayer, EphemeralTeamStats stats) {
        this.partner = (team.getFirstPlayer().equals(targetPlayer)) ? team.getSecondPlayer() : team.getFirstPlayer();
        this.winsTogether = stats.getWins();
        this.lossesTogether = stats.getLosses();
    }

    public Integer getMatchesTogether() {
        return winsTogether + lossesTogether;
    }

    public double getWinRate() {
        return ((double) winsTogether) / (winsTogether + lossesTogether);
    }

    public static class PartnerStatsWinRateComparator implements Comparator<PartnerStats> {
        @Override
        public int compare(PartnerStats s1, PartnerStats s2) {
            return Double.compare(s2.getWinRate(), s1.getWinRate());
        }
    }

    public static class PartnerStatsPopularityComparator implements Comparator<PartnerStats> {
        @Override
        public int compare(PartnerStats s1, PartnerStats s2) {
            return Integer.compare(s2.getMatchesTogether(), s1.getMatchesTogether());
        }
    }

}
