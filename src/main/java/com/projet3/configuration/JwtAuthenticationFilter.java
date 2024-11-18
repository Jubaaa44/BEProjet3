package com.projet3.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import com.projet3.controller.AuthController;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtre personnalisé qui s'exécute une seule fois par requête pour gérer l'authentification
 * basée sur un token JWT. Il extrait le token de la requête, le valide, et configure
 * le contexte de sécurité de l'application si le token est valide.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Utilitaire pour la gestion des JWT
    private final JwtUtil jwtUtil;

    // Logger pour afficher des messages de log
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    // Constructeur pour injecter la dépendance JwtUtil
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Méthode principale du filtre, exécutée à chaque requête.
     * @param request la requête HTTP entrante
     * @param response la réponse HTTP sortante
     * @param filterChain la chaîne de filtres à exécuter
     * @throws ServletException si une erreur de servlet se produit
     * @throws IOException si une erreur d'entrée/sortie se produit
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Extraction du token de la requête
        String token = extractTokenFromRequest(request);

        // Validation du token et authentification de l'utilisateur si le token est valide
        if (token != null && isValidToken(token)) {
            Authentication authentication = authenticateUser(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Passage de la requête à la chaîne de filtres suivante
        filterChain.doFilter(request, response);
    }

    /**
     * Méthode pour extraire le token JWT de l'en-tête de la requête.
     * @param request la requête HTTP
     * @return le token JWT ou null s'il n'est pas présent ou mal formé
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Suppression du préfixe "Bearer "
        }
        return null; // Retourne null si le token n'est pas trouvé
    }

    /**
     * Vérifie la validité du token JWT.
     * @param token le token JWT à valider
     * @return true si le token est valide, false sinon
     */
    private boolean isValidToken(String token) {
        try {
            // Extraction de l'email et validation du token
            String email = jwtUtil.getEmailFromToken(token);
            return jwtUtil.validateToken(token, email);
        } catch (Exception e) {
            // En cas d'erreur de validation, un message d'erreur est enregistré
            log.error("Erreur de validation du token", e);
            return false; // Retourne false si le token est invalide
        }
    }

    /**
     * Authentifie l'utilisateur avec le token JWT.
     * @param token le token JWT contenant les informations de l'utilisateur
     * @return un objet Authentication représentant l'utilisateur authentifié
     */
    private Authentication authenticateUser(String token) {
        // Extraction de l'email à partir du token
        String email = jwtUtil.getEmailFromToken(token);
        log.info("Authentification pour l'email : " + email);

        // Crée un objet d'authentification basé sur l'email sans rôles (null)
        return new UsernamePasswordAuthenticationToken(email, null, null);
    }
}
