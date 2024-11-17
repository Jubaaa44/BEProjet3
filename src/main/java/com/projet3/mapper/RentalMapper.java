package com.projet3.mapper;

import com.projet3.dto.RentalDTO;
import com.projet3.entity.RentalEntity;
import com.projet3.entity.UserEntity;

public class RentalMapper {

    // Convertit un RentalEntity en RentalDTO
    public static RentalDTO toDTO(RentalEntity rentalEntity) {
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

        // Mapping des relations simplifiées
        if (rentalEntity.getOwner() != null) {
            rentalDTO.setOwnerId(rentalEntity.getOwner().getId());
        }
        if (rentalEntity.getMessages() != null) {
            rentalDTO.setMessageCount(rentalEntity.getMessages().size());
        }

        return rentalDTO;
    }

    // Convertit un RentalDTO en RentalEntity
    public static RentalEntity toEntity(RentalDTO rentalDTO) {
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

        /*
        if (rentalDTO.getMessages() != null) {
            rentalEntity.setMessages(
                rentalDTO.getMessages().stream()
                    .map(content -> {
                        MessageEntity message = new MessageEntity();
                        message.setContent(content);
                        return message;
                    })
                    .collect(Collectors.toList())
            );
            */
        return rentalEntity;
    }
}
