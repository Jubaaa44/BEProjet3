package com.projet3.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class MessageRequest {

    @Schema(description = "L'ID de la location associée au message.", required = true)
    private Integer rental_id;

    @Schema(description = "L'ID de l'utilisateur qui envoie le message.", required = true)
    private Integer user_id;

    @Schema(description = "Le contenu du message envoyé par l'utilisateur.", required = true)
    private String message;

    // Getters et Setters
    /**
     * Récupère l'ID de la location associée au message.
     * 
     * @return L'ID de la location.
     */
    public Integer getRental_id() {
        return rental_id;
    }

    /**
     * Définit l'ID de la location associée au message.
     * 
     * @param rental_id L'ID de la location à définir.
     */
    public void setRental_id(Integer rental_id) {
        this.rental_id = rental_id;
    }

    /**
     * Récupère l'ID de l'utilisateur qui envoie le message.
     * 
     * @return L'ID de l'utilisateur.
     */
    public Integer getUser_id() {
        return user_id;
    }

    /**
     * Définit l'ID de l'utilisateur qui envoie le message.
     * 
     * @param user_id L'ID de l'utilisateur à définir.
     */
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    /**
     * Récupère le contenu du message.
     * 
     * @return Le contenu du message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Définit le contenu du message.
     * 
     * @param message Le contenu du message à définir.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
