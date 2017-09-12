package com.estaine.elo.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @Column(name = "played_on", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date playedOn;

    private Boolean tournamentGame;
}
