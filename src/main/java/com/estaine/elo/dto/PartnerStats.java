package com.estaine.elo.dto;

import com.estaine.elo.entity.Player;
import lombok.Data;

@Data
public class PartnerStats {
    private Player partner;
    private Double ratingDelta;
    private Integer winsTogether;
    private Integer losesTogether;


}
