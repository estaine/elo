package com.estaine.elo.repository;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends BaseRepository<Game> {

    List<Game> findAllByOrderByPlayedOnAsc();

    @Query("SELECT g FROM Game g WHERE g.redTeamPlayer1 = ?1 OR g.redTeamPlayer2 = ?1 OR g.yellowTeamPlayer1 = ?1 OR g.yellowTeamPlayer2 = ?1 ORDER BY g.playedOn DESC")
    List<Game> findAllByPlayer(Player player);

}
