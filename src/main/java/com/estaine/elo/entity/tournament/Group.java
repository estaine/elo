package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.BaseModel;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "box")
public class Group extends BaseModel {

    private String name;

    @ManyToOne
    private Tournament tournament;

    @OneToMany(mappedBy = "group")
    private List<Team> teams;

    @OneToMany(mappedBy = "group")
    private List<GroupMatch> groupMatches;

    @Override
    public String toString() {
        return name + " in " + tournament.getName() + ". " + teams.size() + " teams.";
    }
}
