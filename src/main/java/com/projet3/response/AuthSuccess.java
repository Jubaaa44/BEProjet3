package com.projet3.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthSuccess {

    @Schema(description = "Le token JWT généré lors de l'authentification.", required = true)
    private String token;

    // Constructeur
    /**
     * Constructeur de la réponse AuthSuccess.
     * 
     * @param token Le token JWT généré après une authentification réussie.
     */
    public AuthSuccess(String token) {
        this.token = token;
    }

    // Getter et Setter
    /**
     * Récupère le token JWT.
     * 
     * @return Le token JWT.
     */
    public String getToken() {
        return token;
    }

    /**
     * Définit le token JWT.
     * 
     * @param token Le token JWT à définir.
     */
    public void setToken(String token) {
        this.token = token;
    }
}
