package com.estaine.elo.service.exception;

public class IncompatibleTeamsException extends SlackRequestValidationException {

    @Override
    public String getMessage() {
        return "The specified teams belong to different groups. Try to register a common match instead.";
    }
}
