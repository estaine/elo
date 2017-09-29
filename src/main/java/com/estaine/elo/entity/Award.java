package com.estaine.elo.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@Entity
public class Award extends BaseModel {

    @Enumerated(EnumType.STRING)
    private AwardType type;

    @Enumerated(EnumType.STRING)
    private AwardLevel level;

    private String name;

    @ManyToOne
    private Player player;

    public Award(AwardType type, AwardLevel level, String name, Player player) {
        this.type = type;
        this.level = level;
        this.name = name;
        this.player = player;
    }

    public String getIconFilename() {
        StringBuilder builder = new StringBuilder();

        switch (type) {
            case WEEK_END_OVERALL_RATING:
                builder.append("star");
                break;
            case WEEK_RATING_DELTA:
                builder.append("hammersickle");
                break;
        }

        builder.append("-");

        switch (level) {
            case GOLD:
                builder.append("gold");
                break;
            case SILVER:
                builder.append("silver");
                break;
            case BRONZE:
                builder.append("bronze");
                break;
        }

        builder.append(".png");

        return builder.toString();
    }

    public enum AwardType {

        WEEK_END_OVERALL_RATING,
        WEEK_RATING_DELTA,
        TOURNAMENT
    }

    public enum AwardLevel {

        GOLD,
        SILVER,
        BRONZE
    }
}
