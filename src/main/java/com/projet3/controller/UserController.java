package com.projet3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.projet3.dto.UserDTO;
import com.projet3.entity.UserEntity;
import com.projet3.mapper.UserMapper;
import com.projet3.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    // Route pour obtenir un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        if (user != null) {
            UserDTO userDTO = UserMapper.toDTO(user); // Conversion de l'entité en DTO
            return ResponseEntity.ok(userDTO); // Renvoie l'utilisateur en format DTO
        } else {
            return ResponseEntity.notFound().build(); // Renvoie 404 si non trouvé
        }
    }
}
