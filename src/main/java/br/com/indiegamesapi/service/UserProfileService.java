package br.com.indiegamesapi.service;

import br.com.indiegamesapi.dto.request.UserProfileRequestDTO;
import br.com.indiegamesapi.exception.NotFoundException;
import br.com.indiegamesapi.model.UserProfile;
import br.com.indiegamesapi.repository.UserProfileRepository;
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
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Perfil de usuário não encontrado, ID: " + id));
    }

    // NOVO: consulta personalizada (JPQL) - perfis cuja bio contém o texto
    public Page<UserProfile> searchByBio(String text, Pageable pageable){
        return repository.searchByBio(text, pageable);
    }

    @Transactional
    public UserProfile create(UserProfileRequestDTO dto){
        UserProfile userProfile = new UserProfile();
        userProfile.setBio(dto.bio());
        userProfile.setCreatedAt(LocalDateTime.now());
        return repository.save(userProfile);
    }

    @Transactional
    public UserProfile update(Long id, UserProfileRequestDTO dto){
        UserProfile updateUserProfile = findById(id);
        updateUserProfile.setBio(dto.bio());
        updateUserProfile.setUpdatedAt(LocalDateTime.now());
        return updateUserProfile;
    }

    @Transactional
    public void delete(Long id){
        UserProfile userProfile = findById(id);
        repository.delete(userProfile);
        // força o DELETE agora; se o perfil pertencer a um usuário, o erro vira 409 no ExceptionHandlerModel
        repository.flush();
    }
}
