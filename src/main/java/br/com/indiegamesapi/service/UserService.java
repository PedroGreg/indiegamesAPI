package br.com.indiegamesapi.service;

import br.com.indiegamesapi.exception.NotFoundException;
import br.com.indiegamesapi.model.User;
import br.com.indiegamesapi.model.UserProfile;
import br.com.indiegamesapi.repository.UserProfileRepository;
import br.com.indiegamesapi.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserProfileRepository userProfileRepository;

    public UserService(UserRepository repository, UserProfileRepository userProfileRepository){
        this.repository = repository;
        this.userProfileRepository = userProfileRepository;
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
        UserProfile profile = new UserProfile();
        if(user.getUserProfile() != null && user.getUserProfile().getBio() != null){
            profile.setBio(user.getUserProfile().getBio());
            profile.setUser(user);
            profile.setCreatedAt(LocalDateTime.now());
            profile = userProfileRepository.save(profile);
        }
        else{
            profile.setBio("");
            profile.setUser(user);
            profile.setCreatedAt(LocalDateTime.now());
            profile = userProfileRepository.save(profile);
        }
        user.setUserProfile(profile);
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
        if (!user.getPassword().equals(updateUser.getPassword())){
            updateUser.setPassword(user.getPassword());
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
