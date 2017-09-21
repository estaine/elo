package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.Game;

import com.estaine.elo.entity.Player;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import javax.persistence.SequenceGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class BoxGame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "box_game_gen")
    @SequenceGenerator(name = "box_game_gen", sequenceName = "box_game_id_seq")
    private Long id;

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
