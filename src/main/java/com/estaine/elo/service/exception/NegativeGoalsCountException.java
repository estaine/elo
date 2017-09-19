package com.estaine.elo.service.exception;

public class NegativeGoalsCountException extends SlackRequestValidationException {

    @Override
    public String getMessage() {
        return "Negative goals count? Really?..";
    }
}
