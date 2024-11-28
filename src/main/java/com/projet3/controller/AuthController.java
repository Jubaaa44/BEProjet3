package com.projet3.controller;

import java.security.Principal;

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
import com.projet3.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "Inscription d'un nouvel utilisateur")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utilisateur inscrit avec succès"),
        @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO userDTO) {
        log.info("Requête d'inscription reçue");
        return authService.register(userDTO);
    }

    @Operation(summary = "Connexion de l'utilisateur")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connexion réussie"),
        @ApiResponse(responseCode = "401", description = "Identifiants invalides")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO userDTO) {
        log.info("Requête de connexion reçue pour l'email: {}", userDTO.getEmail());
        return authService.login(userDTO);
    }

    @Operation(summary = "Récupération des informations de l'utilisateur")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
        @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> me(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (principal == null || principal.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié");
        }

        log.info("Récupération des informations de l'utilisateur connecté : {}", principal.getName());
        UserDTO userDTO = authService.getUserDetailsByEmail(principal.getName());
        if (userDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }
        return ResponseEntity.ok(userDTO);
    }
}
