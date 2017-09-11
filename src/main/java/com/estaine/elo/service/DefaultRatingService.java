package com.estaine.elo.service;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import com.estaine.elo.entity.Stats;
import com.estaine.elo.repository.GameRepository;
import com.estaine.elo.repository.PlayerRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultRatingService implements RatingService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Override
    public Map<Player, Stats> calculateRatings() {

        List<Player> players = playerRepository.findAll();
        List<Game> games = gameRepository.findAllByOrderByPlayedOnAsc();

        Map<Player, Stats> ratings = new HashMap<>();

        for(Player player : players) {
            ratings.put(player, new Stats(100.0, 0));
        }

        for(Game game : games) {
            Player winner1 = (game.getRedTeamGoals() > game.getYellowTeamGoals()) ? game.getRedTeamPlayer1() : game.getYellowTeamPlayer1();
            Player winner2 = (winner1 == game.getRedTeamPlayer1()) ? game.getRedTeamPlayer2() : game.getYellowTeamPlayer2();
            Player loser1 =  (winner1 == game.getRedTeamPlayer1()) ? game.getYellowTeamPlayer1() : game.getRedTeamPlayer1();
            Player loser2 =  (winner1 == game.getRedTeamPlayer1()) ? game.getYellowTeamPlayer2() : game.getRedTeamPlayer2();

            int goalsAgainst = (winner1 == game.getRedTeamPlayer1()) ? game.getYellowTeamGoals() : game.getRedTeamGoals();
            double losingPercents = 8.0 - (goalsAgainst / 3.0);

            double loser1Delta = ratings.get(loser1).getRating() * losingPercents / 100.0;
            double loser2Delta = ratings.get(loser2).getRating() * losingPercents / 100.0;

            ratings.get(winner1).updateRating(ratings.get(winner1).getRating() + (loser1Delta + loser2Delta) / 2.0);
            ratings.get(winner2).updateRating(ratings.get(winner2).getRating() + (loser1Delta + loser2Delta) / 2.0);
            ratings.get(loser1).updateRating(ratings.get(loser1).getRating() - loser1Delta);
            ratings.get(loser2).updateRating(ratings.get(loser2).getRating() - loser2Delta);


            for(Map.Entry<Player, Stats> playerRating : ratings.entrySet()) {
                System.out.println(playerRating.getKey().getUsername() + " " + String.format("%.2f",playerRating.getValue().getRating()));
            }

            System.out.println();
        }

        return ratings;
    }
}
