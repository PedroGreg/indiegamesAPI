package com.apigamesinidie.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPI {
    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI(@Value("$(springdoc.version)") String appVersion){
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new Info()
                        .title("API of game Indies")
                        .version(appVersion)
                        .description("API to manage game indies")
                        .termsOfService("https://swagger.io/terms/")
                        .license(new License().name("MIT").url("https://mit-license.org/"))
                        .contact(new Contact().name("TSI")
                                .url("https://www.senac.com.br")
                                .email("pedro.jcgregorio@senacsp.edu.br"))
                );
    }
}
