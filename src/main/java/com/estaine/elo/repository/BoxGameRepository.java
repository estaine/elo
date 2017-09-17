package com.estaine.elo.repository;

import com.estaine.elo.entity.tournament.BoxGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoxGameRepository extends JpaRepository<BoxGame, Long> {

}
