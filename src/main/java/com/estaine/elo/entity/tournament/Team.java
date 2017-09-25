package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.BaseModel;
import com.estaine.elo.entity.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
public class Team extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "p1_id")
    private Player player1;

    @ManyToOne
    @JoinColumn(name = "p2_id")
    private Player player2;

    private String name;

    @ManyToOne
    @JoinColumn(name = "box_id")
    private Group group;

    @ManyToOne
    private Tournament tournament;

    @Transient
    private int goalsDelta;

    @Transient
    private int matchesPlayed;

    @Transient
    private int points;

    public void registerMatch(int delta) {
        goalsDelta += delta;
        matchesPlayed++;
        if (delta > 0) {
            points++;
        }
    }

    public boolean consistsOf(Player player1, Player player2) {
        return (player1.equals(this.player1) && player2.equals(this.player2))
                || (player1.equals(this.player2) && player2.equals(this.player1));
    }

    @Override
    public String toString() {
        return "Team " + name + ": " + player1.getUsername() + ", " + player2.getUsername()
                + ". Pts: " + points + ". G+-: " + goalsDelta;
    }
}
