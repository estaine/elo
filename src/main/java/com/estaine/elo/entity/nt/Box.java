package com.estaine.elo.entity.nt;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Box {

    @Id
    @GeneratedValue
    private Long id;

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
