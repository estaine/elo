package com.estaine.elo.service.exception;

public class DrawResultException extends SlackRequestValidationException {

    @Override
    public String getMessage() {
        return "Draws are not supported";
    }
}
