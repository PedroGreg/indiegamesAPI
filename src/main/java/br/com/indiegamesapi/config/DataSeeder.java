package br.com.indiegamesapi.config;

import br.com.indiegamesapi.model.*;
import br.com.indiegamesapi.repository.CategoryRepository;
import br.com.indiegamesapi.repository.DeveloperRepository;
import br.com.indiegamesapi.repository.GameRepository;
import br.com.indiegamesapi.repository.PlatformRepository;
import br.com.indiegamesapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// NOVO: cria dados de exemplo toda vez que a API sobe (o H2 em memória começa vazio).
// CommandLineRunner: o Spring chama o run() uma vez, logo depois que a aplicação subiu.
// @ConditionalOnProperty: só roda se app.seed.enabled=true no application.properties.
@Component
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final CategoryRepository categoryRepository;
    private final DeveloperRepository developerRepository;
    private final PlatformRepository platformRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(CategoryRepository categoryRepository, DeveloperRepository developerRepository,
                      PlatformRepository platformRepository, GameRepository gameRepository,
                      UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.categoryRepository = categoryRepository;
        this.developerRepository = developerRepository;
        this.platformRepository = platformRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        LocalDateTime now = LocalDateTime.now();

        Category rpg = categoryRepository.save(new Category(null, now, "RPG"));
        Category metroidvania = categoryRepository.save(new Category(null, now, "Metroidvania"));
        Category roguelike = categoryRepository.save(new Category(null, now, "Roguelike"));

        Developer tobyFox = developer("Toby Fox", now);
        Developer teamCherry = developer("Team Cherry", now);
        Developer supergiant = developer("Supergiant Games", now);

        Platform pc = platform("PC (Steam)", now);
        Platform sw = platform("Nintendo Switch", now);
        Platform ps = platform("PlayStation 5", now);

        // new ArrayList<>(...): o Hibernate precisa de uma lista que possa ser alterada depois (List.of é imutável)
        gameRepository.save(new Game("Undertale", GameStatus.RELEASED, tobyFox, rpg, new ArrayList<>(List.of(pc, sw)), now, null));
        gameRepository.save(new Game("Deltarune", GameStatus.EARLY_ACCESS, tobyFox, rpg, new ArrayList<>(List.of(pc, sw, ps)), now, null));
        gameRepository.save(new Game("Hollow Knight", GameStatus.RELEASED, teamCherry, metroidvania, new ArrayList<>(List.of(pc, sw, ps)), now, null));
        gameRepository.save(new Game("Hades", GameStatus.RELEASED, supergiant, roguelike, new ArrayList<>(List.of(pc, sw, ps)), now, null));

        // Usuário para testar o login (senha salva como hash BCrypt, igual ao cadastro normal)
        User admin = new User();
        admin.setName("Administrador");
        admin.setEmail("admin@indiegames.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setCreatedAt(now);
        UserProfile profile = new UserProfile();
        profile.setBio("Conta de testes da API");
        profile.setCreatedAt(now);
        profile.setUser(admin);
        admin.setUserProfile(profile);
        userRepository.save(admin);

        log.info("DataSeeder: dados de exemplo criados. Login de teste -> admin@indiegames.com / admin123");
    }

    private Developer developer(String name, LocalDateTime now) {
        Developer developer = new Developer();
        developer.setName(name);
        developer.setCreatedAt(now);
        return developerRepository.save(developer);
    }

    private Platform platform(String name, LocalDateTime now) {
        Platform platform = new Platform();
        platform.setName(name);
        platform.setCreatedAt(now);
        return platformRepository.save(platform);
    }
}
