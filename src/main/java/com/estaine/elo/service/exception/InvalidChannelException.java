package com.estaine.elo.service.exception;

public class InvalidChannelException extends SlackRequestValidationException {

    private String acceptedChannelName;

    public InvalidChannelException(String acceptedChannelName) {
        this.acceptedChannelName = acceptedChannelName;
    }

    @Override
    public String getMessage() {
        return "Matches can be only registered from channel #" + acceptedChannelName;
    }
}
