package com.estaine.elo.repository;

import com.estaine.elo.entity.Player;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends BaseRepository<Player> {

    Player findByUsername(String username);

    Player findBySlackId(String slackId);

}
