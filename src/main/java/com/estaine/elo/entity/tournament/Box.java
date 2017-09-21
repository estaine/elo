package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
public class Box extends BaseModel {

    private String name;

    @ManyToOne
    private Tournament tournament;

    @OneToMany(mappedBy = "box")
    private List<Team> teams;

    @OneToMany(mappedBy = "box")
    private List<BoxGame> boxGames;

    @Override
    public String toString() {
        return name + " in " + tournament.getName() + ". " + teams.size() + " teams.";
    }
}
