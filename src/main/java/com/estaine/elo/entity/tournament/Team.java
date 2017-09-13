package com.estaine.elo.entity.tournament;

import com.estaine.elo.entity.Player;
import lombok.Data;

@Data
public class Team {
    private String name;
    private Player player1;
    private Player player2;
    private int goalsDelta;
    private int gamesPlayed;
    private int points;

    public void registerGame(int delta) {
        goalsDelta += delta;
        gamesPlayed++;
        if (delta > 0) {
            points++;
        }
    }

    public String getDisplayName() {
        return player1.getUsername() + " & " + player2.getUsername();
    }

    public boolean consistsOf(Player player1, Player player2) {
        return (this.player1.equals(player1) && this.player2.equals(player2))
                || (this.player1.equals(player2) && this.player2.equals(player1));
    }
}
