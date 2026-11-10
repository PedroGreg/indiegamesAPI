package br.com.indiegamesapi.service;

import br.com.indiegamesapi.dto.request.CategoryRequestDTO;
import br.com.indiegamesapi.exception.NotFoundException;
import br.com.indiegamesapi.model.Category;
import br.com.indiegamesapi.repository.CategoryRepository;
import br.com.indiegamesapi.repository.GameRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CategoryService {

    private final CategoryRepository repository;
    // NOVO (v2): usado para contar quantos games cada categoria tem
    private final GameRepository gameRepository;

    public CategoryService(CategoryRepository repository, GameRepository gameRepository){
        this.repository = repository;
        this.gameRepository = gameRepository;
    }

    public Page<Category> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Category findById(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Categoria não encontrada, ID: " + id));
    }

    @Transactional
    public Category create(CategoryRequestDTO dto){
        Category category = new Category();
        category.setName(dto.name());
        category.setCreatedAt(LocalDateTime.now());
        return repository.save(category);
    }

    @Transactional
    public Category update(Long id, CategoryRequestDTO dto){
        Category updateCategory = findById(id);
        updateCategory.setName(dto.name());
        updateCategory.setUpdatedAt(LocalDateTime.now());
        return updateCategory;
    }

    public Page<Category> findByName(String name, Pageable pageable){
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    public void delete(Long id){
        Category category = findById(id);
        repository.delete(category);
        // força o DELETE agora; se estiver em uso por um game, o erro vira 409 no ExceptionHandlerModel
        repository.flush();
    }

    // NOVO (v2): quantidade de games da categoria
    public long countGames(Long categoryId){
        return gameRepository.countByCategoryId(categoryId);
    }
}
