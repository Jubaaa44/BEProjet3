package com.projet3.mapper;

import com.projet3.dto.RentalDTO;
import com.projet3.entity.RentalEntity;
import com.projet3.entity.UserEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public class RentalMapper {

    /**
     * Convertit un objet RentalEntity en RentalDTO.
     * 
     * @param rentalEntity L'entité RentalEntity à convertir.
     * @return Le RentalDTO résultant de la conversion, ou null si l'entité est null.
     */
    @Operation(summary = "Convertir un RentalEntity en RentalDTO", 
               description = "Cette méthode mappe un RentalEntity en un RentalDTO, extrayant les informations "
                            + "nécessaires comme le nom, la surface, le prix, l'image, et le nombre de messages associés.")
    public static RentalDTO toDTO(
        @Parameter(description = "L'entité RentalEntity à convertir.", required = true) RentalEntity rentalEntity) {
        
        if (rentalEntity == null) {
            return null;
        }

        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setId(rentalEntity.getId());
        rentalDTO.setName(rentalEntity.getName());
        rentalDTO.setSurface(rentalEntity.getSurface());
        rentalDTO.setPrice(rentalEntity.getPrice());
        rentalDTO.setPicture(rentalEntity.getPicture());
        rentalDTO.setDescription(rentalEntity.getDescription());

        // Mapping des relations simplifiées (propriétaire et messages)
        if (rentalEntity.getOwner() != null) {
            rentalDTO.setOwnerId(rentalEntity.getOwner().getId());
        }
        if (rentalEntity.getMessages() != null) {
            rentalDTO.setMessageCount(rentalEntity.getMessages().size());
        }

        return rentalDTO;
    }

    /**
     * Convertit un RentalDTO en RentalEntity.
     * 
     * @param rentalDTO Le RentalDTO à convertir.
     * @return L'entité RentalEntity résultante de la conversion, ou null si le DTO est null.
     */
    @Operation(summary = "Convertir un RentalDTO en RentalEntity", 
               description = "Cette méthode mappe un RentalDTO en une entité RentalEntity, "
                            + "en associant les IDs des entités liées, comme le propriétaire.")
    public static RentalEntity toEntity(
        @Parameter(description = "Le RentalDTO à convertir.", required = true) RentalDTO rentalDTO) {
        
        if (rentalDTO == null) {
            return null;
        }

        RentalEntity rentalEntity = new RentalEntity();
        rentalEntity.setId(rentalDTO.getId());
        rentalEntity.setName(rentalDTO.getName());
        rentalEntity.setSurface(rentalDTO.getSurface());
        rentalEntity.setPrice(rentalDTO.getPrice());
        rentalEntity.setPicture(rentalDTO.getPicture());
        rentalEntity.setDescription(rentalDTO.getDescription());

        // Note : Pour l'owner et les messages, il faudrait récupérer les entités liées en base
        if (rentalDTO.getOwnerId() != null) {
            UserEntity owner = new UserEntity();
            owner.setId(rentalDTO.getOwnerId());
            rentalEntity.setOwner(owner);
        }

        return rentalEntity;
    }
}
