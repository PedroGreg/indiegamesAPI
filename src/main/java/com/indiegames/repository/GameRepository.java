package com.indiegames.repository;

import com.indiegames.model.Game;
import com.indiegames.model.GameStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameRepository extends BaseRepository<Game>{
    Page<Game> findByStatus(GameStatus status, Pageable pageable);
}
