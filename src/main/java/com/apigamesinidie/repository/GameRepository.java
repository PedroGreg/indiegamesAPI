package com.apigamesinidie.repository;

import com.apigamesinidie.model.Game;
import com.apigamesinidie.model.GameStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameRepository extends BaseRepository<Game>{
    Page<Game> findByStatus(GameStatus status, Pageable pageable);
}
