package com.estaine.elo.entity.nt;

import com.estaine.elo.entity.Game;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class BoxGame {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "red_team_id")
    private Team redTeam;

    @ManyToOne
    @JoinColumn(name = "yellow_team_id")
    private Team yellowTeam;

    @ManyToOne
    private Box box;

    @ManyToOne
    private Game game;

    public BoxGame(Team redTeam, Team yellowTeam, Box box) {
        this.redTeam = redTeam;
        this.yellowTeam = yellowTeam;
        this.box = box;
    }
}
