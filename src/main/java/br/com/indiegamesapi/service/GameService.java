package br.com.indiegamesapi.service;

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
import java.util.List;

@Service
public class GameService {

    private final CategoryRepository categoryRepository;
    private final PlatformRepository platformRepository;
    private final DeveloperRepository developerRepository;
    private final GameRepository repository;

    public GameService(PlatformRepository platformRepository , CategoryRepository categoryRepository, DeveloperRepository developerRepository, GameRepository repository){
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.platformRepository = platformRepository;
        this.developerRepository = developerRepository;
    }

    public Page<Game> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Game findById(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Game not Found, ID: " + id));
    }

    @Transactional
    public Game create(Game game){
        Category category = categoryRepository.findById(game.getCategory().getId())
                .orElseThrow(() -> new NotFoundException("Category not Found, ID: " + game.getCategory().getId()));
        List<Platform> platform = platformRepository.findAllById(game.getPlatforms().stream()
                .map(platform1 -> { return platform1.getId(); }).toList());
        Developer developer = developerRepository.findById(game.getDeveloper().getId())
                .orElseThrow(() -> new NotFoundException("Developer not Found, ID: " + game.getDeveloper().getId()));
        game.setCategory(category);
        game.setDeveloper(developer);
        game.setPlatforms(platform);
        game.setCreatedAt(LocalDateTime.now());
        return repository.save(game);
    }

    @Transactional
    public Game update(Long id, Game game){
        Game updateGame = findById(id);
        Category category = categoryRepository.findById(game.getCategory().getId())
                .orElseThrow(() -> new NotFoundException("Category not Found, ID: " + game.getCategory().getId()));
        List<Platform> platform = platformRepository.findAllById(game.getPlatforms().stream()
                .map(platform1 -> { return platform1.getId(); }).toList());
        Developer developer = developerRepository.findById(game.getDeveloper().getId())
                .orElseThrow(() -> new NotFoundException("Developer not Found, ID: " + game.getDeveloper().getId()));
        if(game.getStatus().equals(GameStatus.DEVELOPMENT) || game.getStatus().equals(GameStatus.EARLY_ACCESS) || game.getStatus().equals(GameStatus.RELEASED)){
            updateGame.setStatus(game.getStatus());
        }else{
            throw new IllegalStateException("Game status not set");
        }
        updateGame.setCategory(category);
        updateGame.setPlatforms(platform);
        updateGame.setDeveloper(developer);
        updateGame.setStatus(game.getStatus());
        updateGame.setName(game.getName());
        updateGame.setUpdatedAt(LocalDateTime.now());
        return updateGame;
    }

    public Page<Game> findByName(String name, Pageable pageable){
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    public void delete(Long id){
        Game game = findById(id);
        repository.delete(game);
    }
}
