package com.projet3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projet3.dto.UserDTO;
import com.projet3.entity.UserEntity;
import com.projet3.mapper.UserMapper;
import com.projet3.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Obtenir un utilisateur par son ID", 
               description = "Retourne les informations de l'utilisateur sous forme de DTO en fonction de l'ID fourni.")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "ID de l'utilisateur", example = "1") @PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        if (user != null) {
            UserDTO userDTO = UserMapper.toDTO(user); // Conversion de l'entité en DTO
            return ResponseEntity.ok(userDTO); // Renvoie l'utilisateur en format DTO
        } else {
            return ResponseEntity.notFound().build(); // Renvoie 404 si non trouvé
        }
    }
}
