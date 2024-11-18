package com.projet3.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "rentals")
public class RentalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique de la location", example = "1")
    private Integer id;

    @Column(nullable = false, length = 255)
    @Schema(description = "Nom de la location", example = "Maison de plage")
    private String name;

    @Schema(description = "Surface de la location en mètres carrés", example = "150.0")
    private Double surface;

    @Schema(description = "Prix par nuit de la location", example = "250.0")
    private Double price;

    @Column(length = 255)
    @Schema(description = "URL ou chemin vers l'image de la location", example = "/images/maisonplage.jpg")
    private String picture;

    @Column(length = 2000)
    @Schema(description = "Description détaillée de la location", example = "Une belle maison près de la plage, idéale pour des vacances d'été.")
    private String description;

    @Column(name = "created_at")
    @Schema(description = "Date et heure de création de la location", example = "2024-11-18T14:30:00")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Date et heure de dernière mise à jour de la location", example = "2024-11-18T15:00:00")
    private LocalDateTime updatedAt;

    // Relation avec l'entité User (propriétaire)
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @Schema(description = "Entité User représentant le propriétaire de la location", example = "2")
    private UserEntity owner;

    // Relation avec les messages
    @OneToMany(mappedBy = "rental")
    @Schema(description = "Liste des messages associés à cette location", example = "[{\"id\":1, \"message\":\"Super endroit !\"}]")
    private List<MessageEntity> messages;

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }
}
