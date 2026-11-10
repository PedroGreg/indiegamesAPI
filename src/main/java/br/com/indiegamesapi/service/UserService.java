package br.com.indiegamesapi.service;

import br.com.indiegamesapi.dto.request.UserRequestDTO;
import br.com.indiegamesapi.exception.BadRequestException;
import br.com.indiegamesapi.exception.NotFoundException;
import br.com.indiegamesapi.model.User;
import br.com.indiegamesapi.model.UserProfile;
import br.com.indiegamesapi.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository repository;
    // NOVO: transforma a senha num hash BCrypt antes de salvar (bean criado no SecurityConfig)
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<User> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public User findById(Long id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado, ID: " + id));
    }

    @Transactional
    public User create(UserRequestDTO dto){
        validateEmail(dto.email(), null);
        // NOVO: no cadastro a senha é obrigatória, senão o usuário nunca conseguiria fazer login
        if (dto.password() == null || dto.password().isBlank()) {
            throw new BadRequestException("password: A password é obrigatória no cadastro");
        }
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        // NOVO: salva o HASH da senha, nunca a senha pura
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setCreatedAt(LocalDateTime.now());

        UserProfile profile = new UserProfile();
        profile.setBio(dto.bio() != null ? dto.bio() : "");
        profile.setUser(user);
        profile.setCreatedAt(LocalDateTime.now());
        user.setUserProfile(profile);
        return repository.save(user);
    }

    @Transactional
    public User update(Long id, UserRequestDTO dto){
        User updateUser = findById(id);
        validateEmail(dto.email(), id);
        updateUser.setName(dto.name());
        updateUser.setEmail(dto.email());
        // NOVO: se veio senha nova, guarda o hash dela; se não veio, mantém a atual
        if (dto.password() != null && !dto.password().isBlank()){
            updateUser.setPassword(passwordEncoder.encode(dto.password()));
        }
        if (dto.bio() != null){
            UserProfile profile = updateUser.getUserProfile();
            if (profile == null){
                profile = new UserProfile();
                profile.setCreatedAt(LocalDateTime.now());
                profile.setUser(updateUser);
                updateUser.setUserProfile(profile);
            }
            profile.setBio(dto.bio());
            profile.setUpdatedAt(LocalDateTime.now());
        }
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

    // Impede e-mail duplicado (o login busca o usuário pelo e-mail)
    private void validateEmail(String email, Long currentUserId){
        repository.findByEmailIgnoreCase(email)
                .filter(existing -> !existing.getId().equals(currentUserId))
                .ifPresent(existing -> { throw new BadRequestException("email: Email já cadastrado: " + email); });
    }
}
