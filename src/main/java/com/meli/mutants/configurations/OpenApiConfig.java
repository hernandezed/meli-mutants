package com.meli.mutants.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {

    @Bean
    public OpenAPI apiDefinition() {
        return new OpenAPI().info(new Info()
                .title("Mutants Api")
                .contact(new Contact().name("Eduardo Hernandez").email("hernandezed.1991@gmail.com"))
                .description("Mutant discovery service for Magneto"));
    }
}
