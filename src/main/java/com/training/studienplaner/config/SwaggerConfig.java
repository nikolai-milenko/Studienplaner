package com.training.studienplaner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI studienplanerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Studienplaner API")
                        .description("Backend API für das Studienplaner-Projekt")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Nikolai Milenko")
                                .url("https://github.com/nikolai-milenko/Studienplaner")
                                .email("nikolai.milenko@gmail.com")
                        )
                );
    }
}

