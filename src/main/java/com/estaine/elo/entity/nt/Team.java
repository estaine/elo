package com.estaine.elo.entity.nt;

import com.estaine.elo.entity.Player;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import lombok.Data;
import org.hibernate.annotations.JoinColumnOrFormula;

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
}
