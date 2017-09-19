package com.estaine.elo.service.exception;

public class MaxGoalsLimitExceededException extends SlackRequestValidationException {

    @Override
    public String getMessage() {
        return "Goals count should be no more than 10 for each team";
    }
}
