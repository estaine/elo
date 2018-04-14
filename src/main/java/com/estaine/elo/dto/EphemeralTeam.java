package com.estaine.elo.dto;

import com.estaine.elo.entity.Player;
import lombok.Data;

@Data
public class EphemeralTeam {

    private Player firstPlayer;

    private Player secondPlayer;

    public EphemeralTeam(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }


    @Override
    public boolean equals(Object o) {
        EphemeralTeam team = (EphemeralTeam) o;

        return (team.firstPlayer.getId().equals(this.firstPlayer.getId()) && team.secondPlayer.getId().equals(this.secondPlayer.getId()))
                || (team.firstPlayer.getId().equals(this.secondPlayer.getId()) && team.secondPlayer.getId().equals(this.firstPlayer.getId()));
    }

    @Override
    public int hashCode() {
        int min = firstPlayer.hashCode() < secondPlayer.hashCode() ? firstPlayer.hashCode() : secondPlayer.hashCode();
        int max = firstPlayer.hashCode() < secondPlayer.hashCode() ? secondPlayer.hashCode() : firstPlayer.hashCode();

        return 31 * min + max;
    }


}
