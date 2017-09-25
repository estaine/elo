package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.BaseModel;
import com.estaine.elo.entity.Match;
import com.estaine.elo.entity.Player;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor

@Entity
@Table(name = "box_game")
public class GroupMatch extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "red_team_id")
    private Team redTeam;

    @ManyToOne
    @JoinColumn(name = "yellow_team_id")
    private Team yellowTeam;

    @ManyToOne
    @JoinColumn(name = "box_id")
    private Group group;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", unique = true)
    private Match match;

    public GroupMatch(Team redTeam, Team yellowTeam, Group group) {
        this.redTeam = redTeam;
        this.yellowTeam = yellowTeam;
        this.group = group;
    }

    public boolean isPlayed() {
        return match != null;
    }

    public boolean consistsOf(Team redTeam, Team yellowTeam) {
        return redTeam.getId().equals(this.redTeam.getId()) && yellowTeam.getId().equals(this.yellowTeam.getId());
    }

    public List<Player> getAllParticipants() {
        return Arrays.asList(redTeam.getPlayer1(), redTeam.getPlayer2(), yellowTeam.getPlayer1(), yellowTeam.getPlayer2());
    }

    @Override
    public String toString() {
        String toString = redTeam.getName() + " vs " + yellowTeam.getName();
        if(match != null) {
            toString += " " + match.getRedTeamGoals() + ":" + match.getYellowTeamGoals();
        }

        return toString;
    }
}
