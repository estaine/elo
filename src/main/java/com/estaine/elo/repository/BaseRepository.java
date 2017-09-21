package com.estaine.elo.repository;

import com.estaine.elo.entity.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
interface BaseRepository<T extends BaseModel> extends JpaRepository<T, Long> {
}
