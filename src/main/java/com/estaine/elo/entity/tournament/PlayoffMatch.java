package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.BaseModel;
import com.estaine.elo.entity.Match;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

@Entity
@Table(name = "playoff_game")
public class PlayoffMatch extends BaseModel {

    @ManyToOne
    private Team redTeam;

    @ManyToOne
    private Team yellowTeam;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", unique = true)
    private Match match;

    @ManyToOne
    private PlayoffSerie playoffSerie;

    public PlayoffMatch(Team redTeam, Team yellowTeam, PlayoffSerie playoffSerie) {
        this.redTeam = redTeam;
        this.yellowTeam = yellowTeam;
        this.playoffSerie = playoffSerie;
    }

    public boolean isPlayed() {
        return match != null;
    }
}
