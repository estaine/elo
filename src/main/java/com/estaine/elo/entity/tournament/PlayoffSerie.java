package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.BaseModel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

@Entity
public class PlayoffSerie extends BaseModel {

    @ManyToOne
    private Tournament tournament;

    @ManyToOne
    private Team firstTeam;

    @ManyToOne
    private Team secondTeam;

    private Stage stage;

    @ManyToOne
    private PlayoffSerie subsequentWinnerSerie;

    @ManyToOne
    private PlayoffSerie subsequentLoserSerie;

    @OneToMany(mappedBy = "playoffSerie", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<PlayoffMatch> playoffMatches;

    @Transient
    private Integer firstTeamWinCount = 0;

    @Transient
    private Integer secondTeamWinCount = 0;

    private int bestOf;

    public PlayoffSerie(Tournament tournament, Team firstTeam, Team secondTeam, int bestOf, Stage stage) {
       this(tournament, firstTeam, secondTeam, bestOf, stage, null, null);
    }

    public PlayoffSerie(Tournament tournament, Team firstTeam, Team secondTeam, Integer bestOf, Stage stage,
            PlayoffSerie subsequentWinnerSerie, PlayoffSerie subsequentLoserSerie) {
        this.tournament = tournament;
        this.firstTeam = firstTeam;
        this.secondTeam = secondTeam;
        this.bestOf = bestOf;
        this.stage = stage;
        this.subsequentWinnerSerie = subsequentWinnerSerie;
        this.subsequentLoserSerie = subsequentLoserSerie;
        this.playoffMatches = new ArrayList<>();
    }
}
