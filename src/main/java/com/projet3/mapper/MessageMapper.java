package com.projet3.mapper;

import com.projet3.dto.MessageDTO;
import com.projet3.entity.MessageEntity;
import com.projet3.entity.RentalEntity;
import com.projet3.entity.UserEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public class MessageMapper {

    /**
     * Convertit un objet MessageEntity en MessageDTO.
     * 
     * @param messageEntity L'entité MessageEntity à convertir.
     * @return Le MessageDTO résultant de la conversion.
     */
    @Operation(summary = "Convertir un MessageEntity en MessageDTO", 
               description = "Cette méthode prend une entité MessageEntity et la mappe dans un MessageDTO, "
                            + "en extrayant les informations nécessaires, y compris les ID des entités liées (Location et Utilisateur).")
    public static MessageDTO toDTO(
        @Parameter(description = "L'entité MessageEntity à convertir.", required = true) MessageEntity messageEntity) {
        
        MessageDTO dto = new MessageDTO();
        
        // Mapper le message de l'entité vers le DTO
        dto.setMessage(messageEntity.getMessage());
        
        // Mapper les entités RentalEntity et UserEntity par leurs ID dans le DTO
        dto.setRentalId(messageEntity.getRental() != null ? messageEntity.getRental().getId() : null);
        dto.setUserId(messageEntity.getUser() != null ? messageEntity.getUser().getId() : null);

        return dto;
    }

    /**
     * Convertit un MessageDTO en MessageEntity.
     * 
     * @param messageDTO Le MessageDTO à convertir.
     * @return L'entité MessageEntity résultante de la conversion.
     */
    @Operation(summary = "Convertir un MessageDTO en MessageEntity", 
               description = "Cette méthode prend un MessageDTO et le mappe dans une entité MessageEntity, "
                            + "en associant les IDs des entités RentalEntity et UserEntity si présents.")
    public static MessageEntity toEntity(
        @Parameter(description = "Le MessageDTO à convertir.", required = true) MessageDTO messageDTO) {
        
        MessageEntity entity = new MessageEntity();
        
        // Mapper le message du DTO vers l'entité
        entity.setMessage(messageDTO.getMessage());

        // Mapper les IDs des entités dans le DTO vers les entités correspondantes
        if (messageDTO.getRentalId() != null) {
            RentalEntity rental = new RentalEntity();
            rental.setId(messageDTO.getRentalId());
            entity.setRental(rental);
        }

        if (messageDTO.getUserId() != null) {
            UserEntity user = new UserEntity();
            user.setId(messageDTO.getUserId());
            entity.setUser(user);
        }

        return entity;
    }
}
