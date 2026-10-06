package com.apigamesinidie.service;

import com.apigamesinidie.exception.NotFoundException;
import com.apigamesinidie.model.Category;
import com.apigamesinidie.model.Developer;
import com.apigamesinidie.model.Game;
import com.apigamesinidie.model.Platform;
import com.apigamesinidie.repository.CategoryRepository;
import com.apigamesinidie.repository.DeveloperRepository;
import com.apigamesinidie.repository.GameRepository;
import com.apigamesinidie.repository.PlatformRepository;
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
        Category category = categoryRepository.findById(game.getCategory().getId()).orElseThrow(() -> new NotFoundException("Category not Found, ID: " + game.getCategory().getId()));
        List<Platform> platform = platformRepository.findAllById(game.getPlatforms().stream().map(platform1 -> { return platform1.getId(); }).toList());
        Developer developer = developerRepository.findById(game.getDeveloper().getId()).orElseThrow(() -> new NotFoundException("Developer not Found, ID: " + game.getDeveloper().getId()));
        game.setCategory(category);
        game.setDeveloper(developer);
        game.setPlatforms(platform);
        game.setCreatedAt(LocalDateTime.now());
        return repository.save(game);
    }

    @Transactional
    public Game update(Long id, Game game){
        Game updateGame = findById(id);
        updateGame.setName(game.getName());
        updateGame.setUpdatedAt(LocalDateTime.now());
        return updateGame;
    }

    public Page<Game> findByName(String name, Pageable pageable){
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    public void delete(Long id){
        Game game = findById(id);
        repository.delete(game);
    }
}
