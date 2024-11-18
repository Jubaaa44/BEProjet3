package com.projet3.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant Unique du message", example = "1")
    private Integer id;

    @Column(length = 2000)
    @Schema(description = "Contenu du message", example = "This is a message content.")
    private String message;

    @Column(name = "created_at")
    @Schema(description = "Date et Heure à laquelle le message a été créé", example = "2024-11-18T14:30:00")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Date et Heure à laquelle le message a été mis à jour", example = "2024-11-18T15:00:00")
    private LocalDateTime updatedAt;

    // Relation avec l'entité Rental
    @ManyToOne
    @JoinColumn(name = "rental_id")
    @Schema(description = "Rental auquel le message appartient", example = "1")
    private RentalEntity rental;

    // Relation avec l'entité User
    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "User auquel le message appartient", example = "2")
    private UserEntity user;

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RentalEntity getRental() {
        return rental;
    }

    public void setRental(RentalEntity rental) {
        this.rental = rental;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
