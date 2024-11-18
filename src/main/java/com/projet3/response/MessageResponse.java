package com.projet3.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class MessageResponse {

    @Schema(description = "Le message de réponse renvoyé à l'utilisateur.", required = true)
    private String message;

    // Constructeurs
    /**
     * Constructeur par défaut pour la classe MessageResponse.
     */
    public MessageResponse() {}

    /**
     * Constructeur pour initialiser MessageResponse avec un message spécifique.
     * 
     * @param message Le message de réponse à renvoyer.
     */
    public MessageResponse(String message) {
        this.message = message;
    }

    // Getter et Setter
    /**
     * Récupère le message de réponse.
     * 
     * @return Le message de réponse.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Définit le message de réponse.
     * 
     * @param message Le message de réponse à définir.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
