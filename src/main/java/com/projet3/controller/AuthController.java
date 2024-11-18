package com.projet3.controller;

import java.security.Principal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.projet3.dto.LoginDTO;
import com.projet3.dto.RegisterDTO;
import com.projet3.dto.UserDTO;
import com.projet3.entity.UserEntity;
import com.projet3.repository.UserRepository;
import com.projet3.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    /**
     * Inscription d'un nouvel utilisateur.
     * @param userDTO DTO contenant les informations d'inscription.
     * @return ResponseEntity avec le statut d'inscription.
     */
    @Operation(summary = "Inscription d'un nouvel utilisateur", description = "Permet à un nouvel utilisateur de s'inscrire.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur inscrit avec succès"),
        @ApiResponse(responseCode = "400", description = "Requête invalide", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO userDTO) {
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return authService.register(userDTO);
    }

    /**
     * Connexion d'un utilisateur existant.
     * @param userDTO DTO contenant les informations de connexion.
     * @return ResponseEntity avec le statut de connexion et le token JWT si succès.
     */
    @Operation(summary = "Connexion de l'utilisateur", description = "Authentifie l'utilisateur et génère un token JWT.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Connexion réussie, token JWT retourné"),
        @ApiResponse(responseCode = "401", description = "Identifiants invalides", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO userDTO) {
        try {
            log.info("Tentative de connexion pour l'email: {}", userDTO.getEmail());
            return authService.login(userDTO);
        } catch (RuntimeException e) {
            log.error("Erreur dans le processus de login pour l'email: {}", userDTO.getEmail(), e);
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    /**
     * Récupération des informations de l'utilisateur connecté.
     * @param request HttpServletRequest pour accéder aux détails de l'utilisateur connecté.
     * @return UserDTO avec les détails de l'utilisateur.
     */
    @Operation(summary = "Récupération des informations de l'utilisateur", description = "Retourne les détails de l'utilisateur actuellement authentifié.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur trouvé", 
                     content = @Content(schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié", content = @Content),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @Content)
    })
    @GetMapping("/me")
    public UserDTO me(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        
        if (principal == null || principal.getName() == null) {
            log.error("Utilisateur non authentifié");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié");
        }

        log.info("Récupération de l'utilisateur : {}", principal.getName());
        String email = principal.getName();

        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            log.error("Utilisateur non trouvé pour l'email : {}", email);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }

        // Créer le DTO et le renvoyer
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setName(userEntity.getName());
        userDTO.setCreated_at(userEntity.getCreatedAt());
        userDTO.setUpdated_at(userEntity.getUpdatedAt());

        return userDTO;
    }
}
