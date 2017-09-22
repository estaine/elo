package com.estaine.elo.repository;

import com.estaine.elo.entity.Game;
import com.estaine.elo.entity.Player;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends BaseRepository<Game> {

    List<Game> findByPlayedOnLessThanEqualOrderByPlayedOnAsc(LocalDateTime base);
}
