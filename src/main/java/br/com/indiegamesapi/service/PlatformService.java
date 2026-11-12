package br.com.indiegamesapi.service;

import br.com.indiegamesapi.dto.request.PlatformRequestDTO;
import br.com.indiegamesapi.exception.NotFoundException;
import br.com.indiegamesapi.model.Platform;
import br.com.indiegamesapi.repository.PlatformRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PlatformService {

    private final PlatformRepository repository;

    public PlatformService(PlatformRepository repository){
        this.repository = repository;
    }

    public Page<Platform> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Platform findById(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Plataforma não encontrada, ID: " + id));
    }

    @Transactional
    public Platform create(PlatformRequestDTO dto){
        Platform platform = new Platform();
        platform.setName(dto.name());
        platform.setCreatedAt(LocalDateTime.now());
        return repository.save(platform);
    }

    @Transactional
    public Platform update(Long id, PlatformRequestDTO dto){
        Platform updatePlatform = findById(id);
        updatePlatform.setName(dto.name());
        updatePlatform.setUpdatedAt(LocalDateTime.now());
        return updatePlatform;
    }

    public Page<Platform> findByName(String name, Pageable pageable){
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    public void delete(Long id){
        Platform platform = findById(id);
        repository.delete(platform);
        repository.flush();
    }
}
