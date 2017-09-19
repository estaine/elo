package com.estaine.elo.service.exception;

public class InvalidTokenException extends SlackRequestValidationException {

    @Override
    public String getMessage() {
        return "Wrong token. Did you send your request not from Slack?";
    }
}
