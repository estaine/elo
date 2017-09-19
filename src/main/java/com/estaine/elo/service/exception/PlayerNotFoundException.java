package com.estaine.elo.service.exception;

public class PlayerNotFoundException extends SlackRequestValidationException {

    private String username;

    public PlayerNotFoundException(String username) {
        this.username = username;
    }

    @Override
    public String getMessage() {
        return "No player with username " + username + " was found in the DB";
    }
}
