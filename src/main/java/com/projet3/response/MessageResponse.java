package com.projet3.response;

public class MessageResponse {
    private String message;

    // Constructeur
    public MessageResponse() {}

    public MessageResponse(String message) {
        this.message = message;
    }

    // Getter et Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
