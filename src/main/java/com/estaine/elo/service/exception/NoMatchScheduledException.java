package com.estaine.elo.service.exception;

public class NoMatchScheduledException extends SlackRequestValidationException {

    @Override
    public String getMessage() {
        return "No match is planned between the specified teams. Perhaps the tournament is not started yet?";
    }
}
