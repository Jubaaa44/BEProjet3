package com.projet3.repository;

import com.projet3.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    /**
     * Recherche des messages par l'ID de l'utilisateur.
     * 
     * @param userId L'ID de l'utilisateur dont les messages sont recherchés.
     * @return Une liste de MessageEntity associés à cet ID utilisateur.
     */
    @Operation(summary = "Trouver des messages par ID utilisateur", 
               description = "Cette méthode permet de récupérer tous les messages associés à un utilisateur "
                            + "en fonction de son ID.")
    List<MessageEntity> findByUserId(
        @Parameter(description = "L'ID de l'utilisateur dont on cherche les messages.", required = true) Integer userId);

    /**
     * Sauvegarde un message dans la base de données.
     * 
     * @param message L'entité MessageEntity à sauvegarder.
     * @return L'entité MessageEntity sauvegardée.
     */
    @Override
    @Operation(summary = "Sauvegarder un message", 
               description = "Cette méthode permet de sauvegarder un message dans la base de données.")
    MessageEntity save(
        @Parameter(description = "L'entité MessageEntity à sauvegarder.", required = true) MessageEntity message);
}
