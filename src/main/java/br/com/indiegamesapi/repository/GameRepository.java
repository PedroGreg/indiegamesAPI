package br.com.indiegamesapi.repository;

import br.com.indiegamesapi.model.Game;
import br.com.indiegamesapi.model.GameStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends BaseRepository<Game>{
    Page<Game> findByStatus(GameStatus status, Pageable pageable);

    @Query("SELECT g FROM Game g WHERE g.developer.id = :developerId")
    Page<Game> findByDeveloperId(@Param("developerId") Long developerId, Pageable pageable);

    @Query("SELECT g FROM Game g WHERE g.category.id = :categoryId")
    Page<Game> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT g FROM Game g JOIN g.platforms p WHERE p.id = :platformId")
    Page<Game> findByPlatformId(@Param("platformId") Long platformId, Pageable pageable);

    long countByCategoryId(Long categoryId);
}
