package com.estaine.elo.repository;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findAllByOrderByPlayedOnAsc();

    @Query("SELECT g FROM Game g WHERE g.redTeamPlayer1 = ?1 OR g.redTeamPlayer2 = ?1 OR g.yellowTeamPlayer1 = ?1 OR g.yellowTeamPlayer2 = ?1")
    List<Game> findAllByPlayer(Player player);
}
