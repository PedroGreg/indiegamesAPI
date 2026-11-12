package br.com.indiegamesapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class OpenAPI {

    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI(@Value("${springdoc.version:1.0.0}") String appVersion) {
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new Info()
                        .title("Indie Games API")
                        .version(appVersion)
                        .description(loadGuide())
                        .termsOfService("/termos.html")
                        .license(new License().name("MIT").url("https://mit-license.org/"))
                        .contact(new Contact()
                                .name("TSI - SENAC")
                                .url("https://www.senac.com.br")
                                .email("pedro.jcgregorio@senacsp.edu.br")))
                .externalDocs(new ExternalDocumentation()
                        .description("Guia completo de uso da API")
                        .url("/guia.html"))
                .tags(List.of(
                        new Tag().name("Autenticação").description("Login e logout. O login gera a chave X-API-Key, exigida em todos os PUT e DELETE."),
                        new Tag().name("v1 - Categorias").description("Categorias (gêneros) dos games."),
                        new Tag().name("v1 - Desenvolvedores").description("Estúdios ou pessoas que desenvolvem os games."),
                        new Tag().name("v1 - Plataformas").description("Plataformas onde os games estão disponíveis."),
                        new Tag().name("v1 - Games").description("Games indie com desenvolvedor, categoria, plataformas e status."),
                        new Tag().name("v1 - Usuários").description("Usuários. O cadastro cria o perfil junto; a senha é guardada como hash BCrypt e nunca é devolvida."),
                        new Tag().name("v1 - Perfis de Usuário").description("Perfis (bio) dos usuários. O perfil é criado junto no cadastro do usuário."),
                        new Tag().name("v2 - Categorias").description("Categorias com a quantidade de games (gamesCount). Só consulta."),
                        new Tag().name("v2 - Games").description("Games resumidos e com o total de conquistas (totalAchievements). Consulta, cadastro e alteração.")
                ));
    }

    private String loadGuide() {
        try (InputStream input = new ClassPathResource("static/guia.md").getInputStream()) {
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "API para gerenciar games indies. Veja o guia em /guia.html";
        }
    }
}
