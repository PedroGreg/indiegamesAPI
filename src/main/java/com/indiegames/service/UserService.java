package com.indiegames.service;

import com.indiegames.exception.NotFoundException;
import com.indiegames.model.User;
import com.indiegames.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public Page<User> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public User findById(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not Found, ID: " + id));
    }

    @Transactional
    public User create(User user){
        user.setCreatedAt(LocalDateTime.now());
        user.getUserProfile().setCreatedAt(LocalDateTime.now());
        user.getUserProfile().setUser(user);
        return repository.save(user);
    }

    @Transactional
    public User update(Long id, User user){
        User updateUser = findById(id);
        if(user.getUserProfile() != null){
            updateUser.setUserProfile(user.getUserProfile());
        }
        updateUser.setName(user.getName());
        updateUser.setUpdatedAt(LocalDateTime.now());
        return updateUser;
    }

    public Page<User> findByName(String name, Pageable pageable){
        return repository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional
    public void delete(Long id){
        User user = findById(id);
        repository.delete(user);
    }
}
