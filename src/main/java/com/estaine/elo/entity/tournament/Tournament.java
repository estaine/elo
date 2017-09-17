package com.estaine.elo.entity.tournament;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Tournament {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Boolean active;

    @OneToMany(mappedBy = "tournament")
    private List<Box> boxes;

    @Override
    public String toString() {
        return name;
    }
}
