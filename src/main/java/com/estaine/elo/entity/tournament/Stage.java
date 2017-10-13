package com.estaine.elo.entity.tournament;

import lombok.Data;
import lombok.Getter;

public enum Stage {

    FINAL(0, "Final"),
    THIRD_PLACE_MATCH(1, "3rd place match"),
    SEMIFINAL(2, "Semifinals");


    Stage(int value, String name) {
        this.value = value;
        this.name = name;
    }

    @Getter
    int value;
    @Getter
    String name;

    public static Stage fromInt(int value) {
        for (Stage stage : Stage.values()) {
            if (stage.value == value) {
                return stage;
            }
        }

        return null;
    }

}
