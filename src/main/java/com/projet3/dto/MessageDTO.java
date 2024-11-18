package com.projet3.dto;

public class MessageDTO {
    private String message;
    private Integer rental_id;
    private Integer user_id;

    // Getters et Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRentalId() {
        return rental_id;
    }

    public void setRentalId(Integer rentalId) {
        this.rental_id = rentalId;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer userId) {
        this.user_id = userId;
    }
}
