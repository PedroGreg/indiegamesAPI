package com.indiegames.config;

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
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new Info()
                        .title("🎮 Indie Games Management API")
                        .version(appVersion)
                        .description("|| API para gerenciar games indies, pode-se cadastrar desenvolvedor, categorias, plataformas, usuários e jogos || URI base para consumo da API: http://localhost:8080/api/ ||")
                        .termsOfService("https://swagger.io/terms/")
                        .license(new License().name("MIT").url("https://mit-license.org/"))
                        .contact(new Contact()
                                .name("TSI - SENAC")
                                .url("https://www.senac.com.br")
                                .email("pedro.jcgregorio@senacsp.edu.br"))
                );
    }
}
