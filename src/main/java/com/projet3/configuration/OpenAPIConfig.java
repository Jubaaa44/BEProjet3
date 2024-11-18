package com.projet3.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configuration pour OpenAPI (Swagger) afin de documenter et sécuriser l'API REST.
 */
@Configuration
public class OpenAPIConfig {

    // Constructeur par défaut
    public OpenAPIConfig() {};

    /**
     * Crée un schéma de sécurité pour l'authentification via JWT.
     * @return un objet SecurityScheme configuré pour l'authentification de type Bearer.
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP) // Définit le type de sécurité HTTP
            .bearerFormat("JWT") // Indique que le format du token est JWT
            .scheme("bearer"); // Utilise le schéma "bearer" pour la transmission du token
    }

    /**
     * Configure l'objet OpenAPI pour spécifier les informations sur l'API et la sécurité.
     * @return un objet OpenAPI configuré avec les détails de l'API et les paramètres de sécurité.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            // Ajoute un élément de sécurité à l'API pour l'authentification Bearer
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            // Configure les composants de l'API pour inclure le schéma de sécurité
            .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
            // Configure les informations générales de l'API (titre, description, version, contact, licence)
            .info(new Info()
                .title("Projet 3 OC - BackEnd") // Titre de l'API
                .description("Cette API est le backend du projet 3 du cours Openclassrooms sur le parcours Développeur Full-Stack ") // Description de l'API
                .version("1.0") // Version de l'API
                .contact(new Contact()
                    .name("Juba ABBACI") // Nom du contact
                    .email("j.abbaci@free.fr") // Email du contact
                    .url("")) // URL de contact
                .license(new License()
                    .name("License of API") // Nom de la licence
                    .url("API license URL"))); // URL de la licence
    }
}
