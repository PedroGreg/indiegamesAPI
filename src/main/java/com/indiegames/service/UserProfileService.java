package com.indiegames.service;

import com.indiegames.exception.NotFoundException;
import com.indiegames.model.UserProfile;
import com.indiegames.repository.UserProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository){
        this.repository = repository;
    }

    public Page<UserProfile> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public UserProfile findById(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Category not Found, ID: " + id));
    }

    @Transactional
    public UserProfile create(UserProfile userProfile){
        userProfile.setCreatedAt(LocalDateTime.now());
        return repository.save(userProfile);
    }

    @Transactional
    public UserProfile update(Long id, UserProfile userProfile){
        UserProfile updateUserProfile = findById(id);
        updateUserProfile.setBio(userProfile.getBio());
        updateUserProfile.setUser(userProfile.getUser());
        updateUserProfile.setUpdatedAt(LocalDateTime.now());
        return updateUserProfile;
    }

    @Transactional
    public void delete(Long id){
        UserProfile userProfile = findById(id);
        repository.delete(userProfile);
    }
}
