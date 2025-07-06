package com.pioner.bankapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public OpenAPI bankAppOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Bank User Management API")
            .description("API for managing bank users and accounts")
            .version("1.0")
            .contact(new Contact()
                .name("Bank Team")
                .url("www.example.com")
                .email("info@example.com"))
            .license(new License()
                .name("Apache 2.0")
                .url("http://springdoc.org")))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components()
            .addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
  }
}