package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.BaseModel;
import java.util.LinkedHashMap;
import javax.persistence.FetchType;
import javax.persistence.Transient;
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
    private List<Group> groups;

    @OneToMany(mappedBy = "tournament")
    private List<Team> teams;

    @OneToMany(mappedBy = "tournament", fetch = FetchType.EAGER)
    private List<PlayoffSerie> playoffSeries;

    @Transient
    private LinkedHashMap<Stage, List<PlayoffSerie>> seriesByStage;

    @Override
    public String toString() {
        return name;
    }
}
