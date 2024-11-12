package com.projet3.mapper;

import com.projet3.entity.UserEntity;
import com.projet3.dto.UserDTO;

public class UserMapper {

    public static UserDTO toDTO(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());

        return dto;
    }

    public static UserEntity toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        // Ne pas définir l'ID ici, car il sera généré automatiquement lors de la sauvegarde
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());
        entity.setPassword(dto.getPassword());  // Si tu as un champ mot de passe

        return entity;
    }
}
