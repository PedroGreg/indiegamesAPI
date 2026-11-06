package br.com.indiegamesapi.repository;

import br.com.indiegamesapi.model.Game;
import br.com.indiegamesapi.model.GameStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameRepository extends BaseRepository<Game>{
    Page<Game> findByStatus(GameStatus status, Pageable pageable);
}
