package br.com.indiegamesapi.service;

import br.com.indiegamesapi.dto.response.LoginResponseDTO;
import br.com.indiegamesapi.exception.UnauthorizedException;
import br.com.indiegamesapi.model.User;
import br.com.indiegamesapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

// NOVO: login com chave de API (X-API-Key).
//
// Como funciona:
//  1. POST /api/auth/login com email e senha.
//  2. Se estiverem certos, geramos uma chave aleatória (UUID), salvamos no usuário e devolvemos.
//  3. Nos DELETE, o controller chama validate(chave): se a chave não for de nenhum usuário -> 401.
//  4. POST /api/auth/logout apaga a chave do usuário (ela para de funcionar).
// Cada login gera uma chave nova e substitui a anterior.
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
        // A mesma mensagem para "email não existe" e "senha errada", para não revelar quais emails estão cadastrados
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UnauthorizedException("Email ou senha inválidos"));

        // matches() compara a senha digitada com o hash BCrypt salvo no banco
        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Email ou senha inválidos");
        }

        // UUID.randomUUID() gera um valor aleatório praticamente impossível de adivinhar
        String apiKey = UUID.randomUUID().toString();
        user.setApiKey(apiKey);
        // Sem save(): dentro do @Transactional, o Hibernate grava a alteração sozinho no fim do método

        return new LoginResponseDTO(apiKey, user.getId(), user.getName(), user.getEmail(),
                "Logado. Envie a apiKey no header X-API-Key para usar os DELETE.");
    }

    // Usado pelos DELETE: garante que a chave enviada pertence a algum usuário. Senão, 401.
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
        // Apaga a chave: a partir de agora ela não funciona mais
        user.setApiKey(null);
    }
}
