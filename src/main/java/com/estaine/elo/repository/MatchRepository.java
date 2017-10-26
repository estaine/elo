package com.estaine.elo.repository;

import com.estaine.elo.entity.Match;
import com.estaine.elo.entity.Player;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends BaseRepository<Match> {

    List<Match> findByPlayedOnLessThanEqualOrderByPlayedOnAsc(LocalDateTime base);
    Match findFirstByOrderByPlayedOnAsc();
    Match findFirstByOrderByPlayedOnDesc();

    @Query("SELECT m FROM Match m WHERE m.redTeamPlayer1 = ?1 OR m.redTeamPlayer2 = ?1 OR m.yellowTeamPlayer1 = ?1 OR m.yellowTeamPlayer2 = ?1 ORDER BY m.playedOn ASC")
    List<Match> findAllByPlayer(Player player);
}
