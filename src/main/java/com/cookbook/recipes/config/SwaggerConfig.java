package com.cookbook.recipes.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Recipe Manager Application",
                description = "An API that allows users to obtain existing information on recipes using specific " +
                        "criteria and adds new recipes, update existing ones or delete old recipes",
                contact = @Contact(
                        name = "Pavel B",
                        url = "",
                        email = "pavel.b@gmail.com"
                ),
                license = @License(
                        name = "License 1.0",
                        url = "http://localhost:8082/licenses/LICENSE-1.0")),
        servers = {
                @Server(url = "http://localhost:8082/api",
                        description = "Development server (uses test data)"),
                @Server(url = "http://localhost:9090/v1",
                        description = "Production server (uses live data)")
        }
)
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public GroupedOpenApi openApi() {
        return GroupedOpenApi.builder()
                .group("Open-Api")
                .packagesToScan("com.cookbook.recipes.web.operations")
                .build();
    }
}
