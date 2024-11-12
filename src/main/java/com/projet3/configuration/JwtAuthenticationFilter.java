
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
/*
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

    	final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
          String token = authorizationHeaderValue.substring(7, authorizationHeaderValue.length());

        log.info("Valeur du token : " + token);
        // Si le token est présent et valide
        if (token != null) {
            // Extraire l'email du token
            String email = jwtUtil.getEmailFromToken(token);
            log.info("Valeur de l'email : " + email);
            if (jwtUtil.validateToken(token, email)) {
            // Créer un objet d'authentification avec l'email
            	log.info("Token VALIDE");
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null, null);
            
            // Mettre l'authentification dans le contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        // Passer la requête au filtre suivant
        filterChain.doFilter(request, response);
    }
    }
    */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractTokenFromRequest(request);

        if (token != null && isValidToken(token)) {
            Authentication authentication = authenticateUser(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response); // Continue with the next filter
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        // Extraction du token de la requête (par exemple dans l'en-tête Authorization)
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Retirer le préfixe "Bearer " et renvoyer le token
        }
        return null;
    }

    private boolean isValidToken(String token) {
        // Vérification de la validité du token JWT
        try {
            String email = jwtUtil.getEmailFromToken(token); // Extraire l'email du token
            return jwtUtil.validateToken(token, email); // Valider le token
        } catch (Exception e) {
            log.error("Erreur de validation du token", e);
            return false;
        }
    }

    private Authentication authenticateUser(String token) {
        // Logique pour authentifier l'utilisateur avec le token
        String email = jwtUtil.getEmailFromToken(token); // Extraire l'email du token
        log.info("Authentification pour l'email : " + email);

        // Créer un objet d'authentification basé sur l'email
        return new UsernamePasswordAuthenticationToken(email, null, null); // L'utilisateur n'a pas de mot de passe ici
    }
}