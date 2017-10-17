package com.estaine.elo.entity.util;

import com.estaine.elo.entity.tournament.Stage;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StageConverter implements AttributeConverter<Stage, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Stage stage) {
        return stage.getValue();
    }

    @Override
    public Stage convertToEntityAttribute(Integer stageNumber) {
        return Stage.fromInt(stageNumber);
    }
}
