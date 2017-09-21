package com.estaine.elo.entity.tournament;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import lombok.Data;

@Data
@Entity
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tournament_gen")
    @SequenceGenerator(name = "tournament_gen", sequenceName = "tournament_id_seq")
    private Long id;

    private String name;

    private Boolean active;

    @OneToMany(mappedBy = "tournament")
    private List<Box> boxes;

    @OneToMany(mappedBy = "tournament")
    private List<Team> teams;

    @Override
    public String toString() {
        return name;
    }
}
