package com.projet3.mapper;

import com.projet3.entity.UserEntity;
import com.projet3.dto.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

public class UserMapper {

    /**
     * Convertit un UserEntity en UserDTO.
     * 
     * @param entity L'entité UserEntity à convertir.
     * @return Le UserDTO résultant de la conversion, ou null si l'entité est null.
     */
    @Operation(summary = "Convertir un UserEntity en UserDTO", 
               description = "Cette méthode mappe un UserEntity en un UserDTO, extrayant les informations "
                            + "de l'utilisateur comme l'email et le nom.")
    public static UserDTO toDTO(
        @Parameter(description = "L'entité UserEntity à convertir.", required = true) UserEntity entity) {
        
        if (entity == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());

        return dto;
    }

    /**
     * Convertit un UserDTO en UserEntity.
     * 
     * @param dto Le UserDTO à convertir.
     * @return L'entité UserEntity résultante de la conversion, ou null si le DTO est null.
     */
    @Operation(summary = "Convertir un UserDTO en UserEntity", 
               description = "Cette méthode mappe un UserDTO en une entité UserEntity, "
                            + "en extrayant les informations comme l'email et le nom.")
    public static UserEntity toEntity(
        @Parameter(description = "Le UserDTO à convertir.", required = true) UserDTO dto) {
        
        if (dto == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());

        return entity;
    }
}
