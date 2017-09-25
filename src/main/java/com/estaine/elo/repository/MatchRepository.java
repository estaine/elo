package com.estaine.elo.repository;

import com.estaine.elo.entity.Match;
import com.estaine.elo.entity.Match;
import com.estaine.elo.entity.Player;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends BaseRepository<Match> {

    List<Match> findByPlayedOnLessThanEqualOrderByPlayedOnAsc(LocalDateTime base);
}
