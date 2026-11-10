package br.com.indiegamesapi.repository;

import br.com.indiegamesapi.model.Game;
import br.com.indiegamesapi.model.GameStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends BaseRepository<Game>{
    // Consulta derivada: o Spring monta o SQL a partir do nome do método ("findBy" + "Status")
    Page<Game> findByStatus(GameStatus status, Pageable pageable);

    // NOVO: consultas personalizadas com @Query (JPQL).
    // JPQL parece SQL, mas usa os nomes das CLASSES e dos CAMPOS Java (Game, g.developer.id),
    // não os das tabelas. ":developerId" é um parâmetro, preenchido pelo @Param de mesmo nome.

    // Games de um desenvolvedor
    @Query("SELECT g FROM Game g WHERE g.developer.id = :developerId")
    Page<Game> findByDeveloperId(@Param("developerId") Long developerId, Pageable pageable);

    // Games de uma categoria
    @Query("SELECT g FROM Game g WHERE g.category.id = :categoryId")
    Page<Game> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    // Games de uma plataforma. platforms é uma LISTA (Many-to-Many), por isso o JOIN para "entrar" nela.
    @Query("SELECT g FROM Game g JOIN g.platforms p WHERE p.id = :platformId")
    Page<Game> findByPlatformId(@Param("platformId") Long platformId, Pageable pageable);

    // NOVO (usado na v2 de categorias): quantos games uma categoria tem.
    // Consulta derivada: "countBy" + "CategoryId" -> SELECT COUNT(*) ... WHERE category.id = ?
    long countByCategoryId(Long categoryId);
}
