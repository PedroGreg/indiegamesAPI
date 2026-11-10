package br.com.indiegamesapi.service;

import br.com.indiegamesapi.dto.request.GameRequestDTO;
import br.com.indiegamesapi.exception.NotFoundException;
import br.com.indiegamesapi.model.*;
import br.com.indiegamesapi.repository.CategoryRepository;
import br.com.indiegamesapi.repository.DeveloperRepository;
import br.com.indiegamesapi.repository.GameRepository;
import br.com.indiegamesapi.repository.PlatformRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    private final CategoryRepository categoryRepository;
    private final PlatformRepository platformRepository;
    private final DeveloperRepository developerRepository;
    private final GameRepository repository;

    public GameService(PlatformRepository platformRepository, CategoryRepository categoryRepository, DeveloperRepository developerRepository, GameRepository repository){
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.platformRepository = platformRepository;
        this.developerRepository = developerRepository;
    }

    public Page<Game> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Game findById(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Game não encontrado, ID: " + id));
    }

    @Transactional
    public Game create(GameRequestDTO dto){
        Game game = new Game();
        applyRequest(game, dto);
        game.setCreatedAt(LocalDateTime.now());
        return repository.save(game);
    }

    @Transactional
    public Game update(Long id, GameRequestDTO dto){
        Game updateGame = findById(id);
        applyRequest(updateGame, dto);
        updateGame.setUpdatedAt(LocalDateTime.now());
        return updateGame;
    }

    public Page<Game> findByName(String name, Pageable pageable){
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    // NOVO: consultas personalizadas (JPQL no GameRepository).
    // Antes de buscar, confere se o desenvolvedor/categoria/plataforma existe: ID inexistente vira 404.
    public Page<Game> findByStatus(GameStatus status, Pageable pageable){
        return repository.findByStatus(status, pageable);
    }

    public Page<Game> findByDeveloper(Long developerId, Pageable pageable){
        if (!developerRepository.existsById(developerId)) {
            throw new NotFoundException("Desenvolvedor não encontrado, ID: " + developerId);
        }
        return repository.findByDeveloperId(developerId, pageable);
    }

    public Page<Game> findByCategory(Long categoryId, Pageable pageable){
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Categoria não encontrada, ID: " + categoryId);
        }
        return repository.findByCategoryId(categoryId, pageable);
    }

    public Page<Game> findByPlatform(Long platformId, Pageable pageable){
        if (!platformRepository.existsById(platformId)) {
            throw new NotFoundException("Plataforma não encontrada, ID: " + platformId);
        }
        return repository.findByPlatformId(platformId, pageable);
    }

    @Transactional
    public void delete(Long id){
        Game game = findById(id);
        repository.delete(game);
    }

    // Código comum do create e do update: busca category, developer e platforms pelos IDs e copia os dados
    private void applyRequest(Game game, GameRequestDTO dto){
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada, ID: " + dto.categoryId()));
        Developer developer = developerRepository.findById(dto.developerId())
                .orElseThrow(() -> new NotFoundException("Desenvolvedor não encontrado, ID: " + dto.developerId()));

        List<Long> platformIds = dto.platformIds().stream().distinct().toList();
        List<Platform> platforms = platformRepository.findAllById(platformIds);
        // findAllById ignora IDs que não existem; aqui eles viram 404
        if (platforms.size() != platformIds.size()) {
            throw new NotFoundException("Uma ou mais plataformas não foram encontradas, IDs enviados: " + platformIds);
        }

        game.setName(dto.name());
        game.setStatus(dto.status());
        game.setCategory(category);
        game.setDeveloper(developer);
        game.setPlatforms(new ArrayList<>(platforms));
    }
}
