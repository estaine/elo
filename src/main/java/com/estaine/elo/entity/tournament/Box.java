package com.estaine.elo.entity.tournament;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import lombok.Data;

@Data
@Entity
public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "box_gen")
    @SequenceGenerator(name = "box_gen", sequenceName = "box_id_seq")
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
