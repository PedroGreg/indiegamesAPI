package com.apigamesinidie.service;

import com.apigamesinidie.exception.NotFoundException;
import com.apigamesinidie.model.Category;
import com.apigamesinidie.model.Developer;
import com.apigamesinidie.repository.CategoryRepository;
import com.apigamesinidie.repository.DeveloperRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DeveloperService {

    private final DeveloperRepository repository;

    public DeveloperService(DeveloperRepository repository){
        this.repository = repository;
    }

    public Page<Developer> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Developer findById(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Category not Found, ID: " + id));
    }

    @Transactional
    public Developer create(Developer developer){
        developer.setCreatedAt(LocalDateTime.now());
        return repository.save(developer);
    }

    @Transactional
    public Developer update(Long id, Developer developer){
        Developer updateDeveloper = findById(id);
        updateDeveloper.setName(developer.getName());
        updateDeveloper.setUpdatedAt(LocalDateTime.now());
        return updateDeveloper;
    }

    public Page<Developer> findByName(String name, Pageable pageable){
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    public void delete(Long id){
        Developer developer = findById(id);
        repository.delete(developer);
    }
}
