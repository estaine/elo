package com.estaine.elo.service.exception;

public class DuplicatingPlayerException extends SlackRequestValidationException {

    @Override
    public String getMessage() {
        return "All players in match should be unique";
    }
}
