package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.Player;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "p1_id")
    private Player player1;

    @ManyToOne
    @JoinColumn(name = "p2_id")
    private Player player2;

    private String name;

    @ManyToOne
    private Box box;

    @ManyToOne
    private Tournament tournament;

    @Transient
    private int goalsDelta;

    @Transient
    private int gamesPlayed;

    @Transient
    private int points;

    public void registerGame(int delta) {
        goalsDelta += delta;
        gamesPlayed++;
        if (delta > 0) {
            points++;
        }
    }

    @Override
    public String toString() {
        return "Team " + name + ": " + player1.getUsername() + ", " + player2.getUsername()
                + ". Pts: " + points + ". G+-: " + goalsDelta;
    }
}
