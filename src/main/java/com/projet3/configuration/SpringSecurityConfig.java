package com.projet3.configuration;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuration de la sécurité Spring pour gérer l'authentification et l'autorisation.
 */
@Configuration
@EnableWebSecurity // Active la sécurité Web de Spring
public class SpringSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Injection de dépendance du filtre d'authentification JWT
    @Autowired
    public SpringSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Classe de configuration de sécurité interne pour définir les règles de sécurité.
     */
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        /**
         * Définit la chaîne de filtres de sécurité pour l'application.
         * @param http l'objet HttpSecurity permettant de configurer la sécurité des requêtes HTTP.
         * @return la configuration de la chaîne de filtres de sécurité.
         * @throws Exception en cas d'erreur de configuration.
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .authorizeHttpRequests(auth -> auth
                    // Permet un accès public aux endpoints de login et d'enregistrement
                    .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                    // Permet un accès public aux pages Swagger et aux ressources téléversées
                    .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/uploads/**").permitAll()
                    // Nécessite une authentification pour certains endpoints protégés
                    .requestMatchers("/api/auth/me", "/api/rentals/**", "api/messages/**").authenticated()
                )
                // Ajoute le filtre d'authentification JWT avant le filtre d'authentification de base de Spring
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Désactive la protection CSRF (utile pour les APIs REST)
                .csrf(csrf -> csrf.disable())
                // Active et configure CORS avec une source spécifique
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

            return http.build();
        }

        /**
         * Configure les paramètres de CORS pour permettre des requêtes cross-origin depuis le frontend Angular.
         * @return la source de configuration CORS.
         */
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Autorise les requêtes du frontend Angular
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Méthodes HTTP autorisées
            configuration.setAllowedHeaders(Arrays.asList("*")); // Tous les en-têtes autorisés
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/api/**", configuration); // Applique cette configuration aux endpoints de l'API
            return source;
        }
    }

    /**
     * Définit un bean de PasswordEncoder utilisant BCrypt pour hacher les mots de passe.
     * @return une instance de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utilisation de BCrypt pour encoder les mots de passe
    }
}
