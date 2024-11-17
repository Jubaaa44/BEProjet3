package com.projet3.mapper;

import com.projet3.dto.MessageDTO;
import com.projet3.entity.MessageEntity;
import com.projet3.entity.RentalEntity;
import com.projet3.entity.UserEntity;

public class MessageMapper {

    // Convertir MessageEntity en MessageDTO
    public static MessageDTO toDTO(MessageEntity messageEntity) {
        MessageDTO dto = new MessageDTO();
        // dto.setId(messageEntity.getId());
        dto.setMessage(messageEntity.getMessage());
        
        // Mapper RentalEntity et UserEntity à leurs IDs dans le DTO
        dto.setRentalId(messageEntity.getRental() != null ? messageEntity.getRental().getId() : null);
        dto.setUserId(messageEntity.getUser() != null ? messageEntity.getUser().getId() : null);

        return dto;
    }

    // Convertir MessageDTO en MessageEntity
    public static MessageEntity toEntity(MessageDTO messageDTO) {
        MessageEntity entity = new MessageEntity();
        // entity.setId(messageDTO.getId());
        entity.setMessage(messageDTO.getMessage());

        // Mapper les IDs dans le DTO aux entités correspondantes (si nécessaires)
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
