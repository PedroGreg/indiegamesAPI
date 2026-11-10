package br.com.indiegamesapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    // NOVO: origens permitidas vêm do application.properties (app.cors.allowed-origins), separadas por vírgula
    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // ALTERADO: antes era allowedOriginPatterns("*"), que liberava qualquer site.
                        // Agora só os front-ends listados (requisito: "permitir acesso de origens específicas").
                        .allowedOrigins(allowedOrigins)
                        // ALTERADO: OPTIONS incluído (é a pergunta "preflight" que o navegador faz antes do DELETE)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // ALTERADO: em vez de "*", só os headers que a API usa
                        .allowedHeaders("Content-Type", "Accept", "X-API-Key", "X-Idempotency-Key")
                        // NOVO: headers da resposta que o JavaScript do front-end pode ler
                        .exposedHeaders("Retry-After", "X-Rate-Limit-Remaining")
                        .allowCredentials(true);
            }
        };
    }
}
