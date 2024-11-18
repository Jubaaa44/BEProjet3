package com.projet3.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique de l'utilisateur", example = "1")
    private Integer id;

    @Column(nullable = false, unique = true, length = 255)
    @Schema(description = "Adresse email unique de l'utilisateur", example = "user@example.com")
    private String email;

    @Column(nullable = false, length = 255)
    @Schema(description = "Nom complet de l'utilisateur", example = "John Doe")
    private String name;

    @Column(nullable = false, length = 255)
    @Schema(description = "Mot de passe de l'utilisateur (stocké de manière sécurisée)", example = "********")
    private String password;

    @Column(name = "created_at")
    @Schema(description = "Date et heure de création de l'utilisateur", example = "2024-11-18T14:30:00")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Date et heure de dernière mise à jour de l'utilisateur", example = "2024-11-18T15:00:00")
    private LocalDateTime updatedAt;

    // Relations avec les entités Rental et Message
    @OneToMany(mappedBy = "owner")
    @Schema(description = "Liste des locations créées par cet utilisateur", example = "[{\"id\":1, \"name\":\"Maison de vacances\"}]")
    private List<RentalEntity> rentals;

    @OneToMany(mappedBy = "user")
    @Schema(description = "Liste des messages associés à cet utilisateur", example = "[{\"id\":1, \"message\":\"Bonjour, j'aimerais en savoir plus sur la location.\"}]")
    private List<MessageEntity> messages;

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<RentalEntity> getRentals() {
        return rentals;
    }

    public void setRentals(List<RentalEntity> rentals) {
        this.rentals = rentals;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }
}
