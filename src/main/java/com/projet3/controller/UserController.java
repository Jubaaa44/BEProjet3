package com.projet3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projet3.dto.UserDTO;
import com.projet3.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
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
            @Parameter(description = "ID de l'utilisateur", example = "1") @PathVariable Integer id) {
        // On suppose que `getUserById` retourne un objet UserDTO ou null si non trouvé
        UserDTO user = userService.getUserById(id);
        
        // Si l'utilisateur existe, on renvoie une réponse 200 avec les données
        if (user != null) {
            return ResponseEntity.ok(user); 
        } else {
            // Si l'utilisateur n'est pas trouvé, on renvoie une réponse 404
            return ResponseEntity.notFound().build(); 
        }
    }
}
