package com.estaine.elo.entity;

import com.estaine.elo.entity.tournament.BoxGame;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "red_team_p1_id")
    private Player redTeamPlayer1;

    @ManyToOne
    @JoinColumn(name = "red_team_p2_id")
    private Player redTeamPlayer2;

    @ManyToOne
    @JoinColumn(name = "yellow_team_p1_id")
    private Player yellowTeamPlayer1;

    @ManyToOne
    @JoinColumn(name = "yellow_team_p2_id")
    private Player yellowTeamPlayer2;

    private int redTeamGoals;

    private int yellowTeamGoals;

    @OneToOne(mappedBy = "game", fetch = FetchType.EAGER)
    private BoxGame boxGame;

    @Column(name = "played_on", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date playedOn;

    public boolean isTournamentGame() {
        return boxGame != null;
    }

    public List<Player> getAllParticipants() {
        return Arrays.asList(redTeamPlayer1, redTeamPlayer2, yellowTeamPlayer1, yellowTeamPlayer2);
    }


    @Override
    public String toString() {
        return redTeamPlayer1.getUsername() + " & " + redTeamPlayer2.getUsername() + " vs "
                + yellowTeamPlayer1.getUsername() + " & " + yellowTeamPlayer2.getUsername() + " "
                + redTeamGoals + ":" + yellowTeamGoals;
    }
}
