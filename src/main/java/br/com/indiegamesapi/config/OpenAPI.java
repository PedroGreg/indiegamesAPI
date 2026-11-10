package br.com.indiegamesapi.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPI {
    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI(@Value("${springdoc.version:1.0.0}") String appVersion) {
        // ALTERADO: removido o esquema "Bearer JWT" (a API não usa JWT); a X-API-Key aparece como campo nos DELETE.
        // A descrição agora explica login, idempotência, versões e rate limit.
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new Info()
                        .title("🎮 Indie Games Management API")
                        .version(appVersion)
                        .description("API para gerenciar games indies. URI base: http://localhost:8080/api/\n\n" +
                                "**Login:** POST /api/auth/login devolve uma apiKey; envie no header X-API-Key dos DELETE.\n\n" +
                                "**Idempotência:** todo POST exige o header X-Idempotency-Key (ex.: um UUID). Reenviar com a mesma chave devolve o registro já criado.\n\n" +
                                "**Versões:** v1 em /api/{recurso} (ou /api/{recurso}/v1); v2 em /api/category/v2 e /api/game/v2.\n\n" +
                                "**Rate limit:** 20 requisições por minuto por IP; acima disso, 429 com o header Retry-After.")
                        .termsOfService("https://swagger.io/terms/")
                        .license(new License().name("MIT").url("https://mit-license.org/"))
                        .contact(new Contact()
                                .name("TSI - SENAC")
                                .url("https://www.senac.com.br")
                                .email("pedro.jcgregorio@senacsp.edu.br")));
    }
}
