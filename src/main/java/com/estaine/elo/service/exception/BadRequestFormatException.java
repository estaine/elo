package com.estaine.elo.service.exception;

public class BadRequestFormatException extends SlackRequestValidationException {

    @Override
    public String getMessage() {
        return "Bad request format. Please format your command according to the example";
    }
}
