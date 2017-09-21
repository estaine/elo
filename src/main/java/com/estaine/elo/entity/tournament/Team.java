package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.Player;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import lombok.Data;

@Data
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "team_gen")
    @SequenceGenerator(name = "team_gen", sequenceName = "team_id_seq")
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
