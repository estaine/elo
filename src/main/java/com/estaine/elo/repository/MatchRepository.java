package com.estaine.elo.repository;

import com.estaine.elo.entity.Match;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends BaseRepository<Match> {

    List<Match> findByPlayedOnLessThanEqualOrderByPlayedOnAsc(LocalDateTime base);
    Match findFirstByOrderByPlayedOnAsc();
    Match findFirstByOrderByPlayedOnDesc();
}
