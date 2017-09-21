package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.BaseModel;
import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

@Entity
public class BoxGame extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "red_team_id")
    private Team redTeam;

    @ManyToOne
    @JoinColumn(name = "yellow_team_id")
    private Team yellowTeam;

    @ManyToOne
    private Box box;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", unique = true)
    private Game game;

    public BoxGame(Team redTeam, Team yellowTeam, Box box) {
        this.redTeam = redTeam;
        this.yellowTeam = yellowTeam;
        this.box = box;
    }

    public boolean isPlayed() {
        return game != null;
    }

    public boolean consistsOf(Team redTeam, Team yellowTeam) {
        return redTeam.getId().equals(this.redTeam.getId()) && yellowTeam.getId().equals(this.yellowTeam.getId());
    }

    public List<Player> getAllParticipants() {
        return Arrays.asList(redTeam.getPlayer1(), redTeam.getPlayer2(), yellowTeam.getPlayer1(), yellowTeam.getPlayer2());
    }

    @Override
    public String toString() {
        String toString = redTeam.getName() + " vs " + yellowTeam.getName();
        if(game != null) {
            toString += " " + game.getRedTeamGoals() + ":" + game.getYellowTeamGoals();
        }

        return toString;
    }
}
