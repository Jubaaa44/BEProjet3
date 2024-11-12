package com.projet3.response;

public class AuthSuccess {
    private String token;

    // Constructeur
    public AuthSuccess(String token) {
        this.token = token;
    }

    // Getter et Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
