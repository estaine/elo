package com.estaine.elo.service.exception;

import java.util.List;
import java.util.stream.Collectors;

public class InvalidChannelException extends SlackRequestValidationException {

    private List<String> acceptedChannelNames;

    public InvalidChannelException(List<String> acceptedChannelNames) {
        this.acceptedChannelNames = acceptedChannelNames;
    }

    @Override
    public String getMessage() {
        return "Matches can be only registered from channel "
                + acceptedChannelNames.stream()
                .map(name -> "#" + name)
                .collect(Collectors.joining(", "));
    }
}
