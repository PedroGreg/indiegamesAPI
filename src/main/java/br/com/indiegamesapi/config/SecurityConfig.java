package br.com.indiegamesapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    // NOVO: "codificador" de senhas, usado no cadastro (UserService) e no login (AuthService).
    // BCrypt transforma a senha num hash de mão única: do hash não dá para voltar à senha.
    // No login usamos passwordEncoder.matches(senhaDigitada, hashDoBanco) para comparar.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
