package com.balybus.galaxy.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {


        return new OpenAPI()
                .info(new Info()
                        .title("Galaxy API")
                        .description("Blaybus Galaxy Project API 문서")
                        .version("1.0.0"));
    }
}

