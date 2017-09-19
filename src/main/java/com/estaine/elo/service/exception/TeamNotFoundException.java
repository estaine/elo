package com.estaine.elo.service.exception;

import com.estaine.elo.entity.Player;

public class TeamNotFoundException extends SlackRequestValidationException {

    private Player player1;
    private Player player2;

    public TeamNotFoundException(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public String getMessage() {
        return "No team consisting of " + player1.getUsername() + " and "
                + player2.getUsername() + " found in current tournament";
    }
}
