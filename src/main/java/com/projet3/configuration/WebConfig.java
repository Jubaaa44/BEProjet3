package com.projet3.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration de Web MVC pour gérer l'exposition des ressources statiques,
 * en particulier les fichiers téléchargés.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure les gestionnaires de ressources pour mapper les chemins URL aux emplacements des ressources.
     * @param registry l'objet ResourceHandlerRegistry pour enregistrer les gestionnaires de ressources.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mappe les requêtes commençant par "/uploads/" à l'emplacement "file:uploads/"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/")
                .setCachePeriod(3600); // Optionnel : spécifie une période de mise en cache en secondes
    }
}
