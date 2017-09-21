package com.estaine.elo.repository;

import com.estaine.elo.entity.tournament.Tournament;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends BaseRepository<Tournament> {

    Tournament findByActiveTrue();

}
