package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
public class Tournament extends BaseModel {

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
