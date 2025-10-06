package com.join.Mcqueen.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do OpenAPI/Swagger para documentação da API
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mcqueen Email Service API")
                        .description("API REST para envio de emails com formulários e anexos CSV. " +
                                "Este serviço permite o envio automatizado de emails contendo dados " +
                                "estruturados em formulários juntamente com arquivos CSV como anexos.")
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("Equipe Mcqueen")
                                .email("gabriel.almeida1@sad.pe.gov.br"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}