package br.com.indiegamesapi.service;

import br.com.indiegamesapi.dto.response.LoginResponseDTO;
import br.com.indiegamesapi.exception.UnauthorizedException;
import br.com.indiegamesapi.model.User;
import br.com.indiegamesapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public LoginResponseDTO login(String email, String password) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UnauthorizedException("Email ou senha inválidos"));

        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Email ou senha inválidos");
        }

        String apiKey = UUID.randomUUID().toString();
        user.setApiKey(apiKey);

        return new LoginResponseDTO(apiKey, user.getId(), user.getName(), user.getEmail(),
                "Logado. Envie a apiKey no header X-API-Key para usar os PUT e DELETE.");
    }

    public User validate(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new UnauthorizedException("Esta operação exige login: envie o header X-API-Key (obtenha a chave em POST /api/auth/login)");
        }
        return userRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new UnauthorizedException("X-API-Key inválida. Faça login novamente em POST /api/auth/login"));
    }

    @Transactional
    public void logout(String apiKey) {
        User user = validate(apiKey);
        user.setApiKey(null);
    }
}
