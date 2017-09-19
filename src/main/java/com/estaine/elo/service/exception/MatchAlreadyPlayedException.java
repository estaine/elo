package com.estaine.elo.service.exception;

public class MatchAlreadyPlayedException extends SlackRequestValidationException {

    @Override
    public String getMessage() {
        return "The specified teams have already played a match. Did you mix red team and yellow team up?";
    }
}
