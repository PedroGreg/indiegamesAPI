package com.indiegames.config;

import com.indiegames.model.*;
import com.indiegames.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(
            CategoryRepository categoryRepository,
            DeveloperRepository developerRepository,
            PlatformRepository platformRepository,
            GameRepository gameRepository,
            UserRepository userRepository
    ) {
        return args -> {
            log.info("Populando o banco de dados H2...");

            LocalDateTime now = LocalDateTime.now();

            // 1. Categorias
            Category rpg = new Category();
            rpg.setName("RPG");
            rpg.setCreatedAt(now);
            rpg.setUpdatedAt(now);
            categoryRepository.save(rpg);

            Category horror = new Category();
            horror.setName("Survival Horror");
            horror.setCreatedAt(now);
            horror.setUpdatedAt(now);
            categoryRepository.save(horror);

            // 2. Plataformas
            Platform pc = new Platform();
            pc.setName("PC (Steam)");
            pc.setCreatedAt(now);
            pc.setUpdatedAt(now);
            platformRepository.save(pc);

            Platform switchPlatform = new Platform();
            switchPlatform.setName("Nintendo Switch");
            switchPlatform.setCreatedAt(now);
            switchPlatform.setUpdatedAt(now);
            platformRepository.save(switchPlatform);

            // 3. Desenvolvedores (Developers)
            Developer tobyFox = new Developer();
            tobyFox.setName("Toby Fox");
            tobyFox.setCreatedAt(now);
            tobyFox.setUpdatedAt(now);
            developerRepository.save(tobyFox);

            Developer scottCawthon = new Developer();
            scottCawthon.setName("Scott Cawthon");
            scottCawthon.setCreatedAt(now);
            scottCawthon.setUpdatedAt(now);
            developerRepository.save(scottCawthon);

            // 4. Jogos (Games)
            Game undertale = new Game();
            undertale.setName("Undertale");
            undertale.setStatus(GameStatus.RELEASED); // Ajuste para a sua Enum se necessário
            undertale.setDeveloper(tobyFox);
            undertale.setCategory(rpg);
            undertale.setPlatforms(List.of(pc, switchPlatform));
            undertale.setCreatedAt(now);
            undertale.setUpdatedAt(now);
            gameRepository.save(undertale);

            Game deltarune = new Game();
            deltarune.setName("Deltarune");
            deltarune.setStatus(GameStatus.EARLY_ACCESS); // Ajuste para a sua Enum se necessário
            deltarune.setDeveloper(tobyFox);
            deltarune.setCategory(rpg);
            deltarune.setPlatforms(List.of(pc, switchPlatform));
            deltarune.setCreatedAt(now);
            deltarune.setUpdatedAt(now);
            gameRepository.save(deltarune);

            Game fnaf = new Game();
            fnaf.setName("Five Nights at Freddy's");
            fnaf.setStatus(GameStatus.RELEASED); // Ajuste para a sua Enum se necessário
            fnaf.setDeveloper(scottCawthon);
            fnaf.setCategory(horror);
            fnaf.setPlatforms(List.of(pc));
            fnaf.setCreatedAt(now);
            fnaf.setUpdatedAt(now);
            gameRepository.save(fnaf);

            // 5. Usuários com Perfis (User & UserProfile)
            User user1 = new User();
            user1.setName("Pedro Gregorio");
            user1.setEmail("pedro@email.com");
            user1.setCreatedAt(now);
            user1.setUpdatedAt(now);

            UserProfile profile1 = new UserProfile();
            profile1.setBio("Desenvolvedor Backend e Entusiasta de Jogos Indies");
            profile1.setCreatedAt(now);
            profile1.setUpdatedAt(now);
            profile1.setUser(user1);
            user1.setUserProfile(profile1);

            userRepository.save(user1);

            User user2 = new User();
            user2.setName("Joy");
            user2.setEmail("joy@email.com");
            user2.setCreatedAt(now);
            user2.setUpdatedAt(now);

            UserProfile profile2 = new UserProfile();
            profile2.setBio("Gamer e colecionador de RPGs indies");
            profile2.setCreatedAt(now);
            profile2.setUpdatedAt(now);
            profile2.setUser(user2);
            user2.setUserProfile(profile2);

            userRepository.save(user2);

            log.info("Banco de dados H2 populado com sucesso!");
        };
    }
}