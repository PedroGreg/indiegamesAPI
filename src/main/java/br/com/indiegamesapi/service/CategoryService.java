package br.com.indiegamesapi.service;

import br.com.indiegamesapi.exception.NotFoundException;
import br.com.indiegamesapi.model.Category;
import br.com.indiegamesapi.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository){
        this.repository = repository;
    }

    public Page<Category> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Category findById(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Category not Found, ID: " + id));
    }

    public Category create(Category category){
        category.setCreatedAt(LocalDateTime.now());
        return repository.save(category);
    }

    public Category update(Long id, Category category){
        Category updateCategory = findById(id);
        updateCategory.setName(category.getName());
        updateCategory.setUpdatedAt(LocalDateTime.now());
        return updateCategory;
    }

    public Page<Category> findByName(String name, Pageable pageable){
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    public void delete(Long id){
        Category category = findById(id);
        repository.delete(category);
    }
}
