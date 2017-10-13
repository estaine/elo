package com.estaine.elo.entity;

import com.estaine.elo.entity.tournament.GroupMatch;
import com.estaine.elo.entity.tournament.GroupMatch;
import com.estaine.elo.entity.tournament.PlayoffMatch;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "game")
public class Match extends BaseModel {

    private static final String DATE_TIME_FORMAT = "E, dd-MMM-yyyy, HH:mm";

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

    @OneToOne(mappedBy = "match", fetch = FetchType.EAGER)
    private GroupMatch groupMatch;

    @OneToOne(mappedBy = "match", fetch = FetchType.EAGER)
    private PlayoffMatch playoffMatch;

    @Column(name = "played_on")
    private LocalDateTime playedOn;

    private String reportedBy;

    public boolean isTournamentMatch() {
        return (groupMatch != null) || (playoffMatch != null);
    }

    public List<Player> getAllParticipants() {
        return Arrays.asList(redTeamPlayer1, redTeamPlayer2, yellowTeamPlayer1, yellowTeamPlayer2);
    }

    public void clearTournamentMatch() {
        groupMatch = null;
        playoffMatch = null;
    }

    @Override
    public String toString() {
        return redTeamPlayer1.getUsername() + " & " + redTeamPlayer2.getUsername()
                + " " + redTeamGoals + ":" + yellowTeamGoals + " "
                + yellowTeamPlayer1.getUsername() + " & " + yellowTeamPlayer2.getUsername()
                + " [" + playedOn.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)) + "]";

    }
}
