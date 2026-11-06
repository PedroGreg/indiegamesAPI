package br.com.indiegamesapi.service;

import br.com.indiegamesapi.exception.BadRequestException;
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
        validateRelations(game);
        Category category = categoryRepository.findById(game.getCategory().getId())
                .orElseThrow(() -> new NotFoundException("Category not Found, ID: " + game.getCategory().getId()));
        List<Platform> platform = platformRepository.findAllById(game.getPlatforms().stream()
                .map(platform1 -> { return platform1.getId(); }).toList());

        if (platform.size() != game.getPlatforms().stream().map(Platform::getId).distinct().count()) {
            throw new NotFoundException("One or more Platforms not Found, IDs: " + game.getPlatforms().stream().map(Platform::getId).toList());
        }
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

        validateRelations(game);
        Category category = categoryRepository.findById(game.getCategory().getId())
                .orElseThrow(() -> new NotFoundException("Category not Found, ID: " + game.getCategory().getId()));
        List<Platform> platform = platformRepository.findAllById(game.getPlatforms().stream()
                .map(platform1 -> { return platform1.getId(); }).toList());

        if (platform.size() != game.getPlatforms().stream().map(Platform::getId).distinct().count()) {

            throw new NotFoundException("One or more Platforms not Found, IDs: " + game.getPlatforms().stream().map(Platform::getId).toList());
        }
        Developer developer = developerRepository.findById(game.getDeveloper().getId())
                .orElseThrow(() -> new NotFoundException("Developer not Found, ID: " + game.getDeveloper().getId()));

        if(game.getStatus() != null && (game.getStatus().equals(GameStatus.DEVELOPMENT) || game.getStatus().equals(GameStatus.EARLY_ACCESS) || game.getStatus().equals(GameStatus.RELEASED))){
            updateGame.setStatus(game.getStatus());
        }else{
            throw new BadRequestException("Game status not set, allowed values: DEVELOPMENT, EARLY_ACCESS, RELEASED");
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

    @Transactional
    public void delete(Long id){
        Game game = findById(id);
        repository.delete(game);
    }

    private void validateRelations(Game game){

        if (game.getCategory() == null || game.getCategory().getId() == null) {
            throw new BadRequestException("Category ID is required");
        }

        if (game.getDeveloper() == null || game.getDeveloper().getId() == null) {
            throw new BadRequestException("Developer ID is required");
        }

        if (game.getPlatforms() == null || game.getPlatforms().stream().anyMatch(p -> p == null || p.getId() == null)) {
            throw new BadRequestException("Platforms list is required and every Platform must have an ID");
        }
    }
}
