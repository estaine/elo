package com.estaine.elo.repository;

import com.estaine.elo.entity.tournament.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    Tournament findByActiveTrue();
}
