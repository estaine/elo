package com.estaine.elo.repository;

import com.estaine.elo.entity.Award;
import com.estaine.elo.entity.Award.AwardType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AwardRepository extends BaseRepository<Award> {

    @Transactional
    void deleteByType(AwardType type);
}
